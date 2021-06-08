package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Role;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.order.OrderDAO;
import com.epam.esm.persistence.dao.tag.TagDAO;
import com.epam.esm.persistence.util.finder.impl.OrderFinder;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.certificate.CertificateService;
import com.epam.esm.service.service.order.OrderServiceImpl;
import com.epam.esm.service.service.user.UserService;
import com.epam.esm.service.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ServiceTestConfig.class)
@Transactional
class OrderServiceImplTests {

    @MockBean
    OrderDAO orderDaoMock;

    @MockBean
    TagDAO tagDaoMock;

    @MockBean
    UserService userServiceMock;

    @MockBean
    CertificateService certificateServiceMock;

    @MockBean
    EntityValidator<Order> validator;

    @Autowired
    OrderServiceImpl service;

    private Order order1;
    private Order order2;
    private Order order3;

    private final Order[] orders = {order1, order2, order3};
    private final Order[] ordersShort = {order1, order2};
    private final List<Order> fullList = Arrays.asList(orders);
    private final List<Order> shortList = Arrays.asList(ordersShort);

    @BeforeEach
    void init() throws ValidationException, NotFoundException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Certificate certificate = new Certificate("nastolka.by",
                BigDecimal.valueOf(40.1), 50);
        certificate.setDescription("board games certificate");
        certificate.setCreateDate(LocalDateTime.parse("2021-01-22 09:20:11", formatter));
        certificate.setLastUpdateDate(LocalDateTime.parse("2021-03-22 09:20:11", formatter));
        certificate.setId(2);

        User user = new User("Ivan", "Ivanov", "Ivanov",
                "$2y$12$nDIbpnb/9S61LuSKF2JFt.AUHSESFw.xPwr/Ie6U6DvACFJuZACuq");
        user.setId(2);
        user.addRole(new Role("USER"));

        order1 = new Order(certificate, user, BigDecimal.valueOf(10.0), 1,
                LocalDateTime.parse("2021-01-22 09:20:11", formatter));
        order1.setId(3);
        order2 = new Order(certificate, user, BigDecimal.valueOf(20.0), 2,
                LocalDateTime.parse("2021-02-22 09:20:11", formatter));
        order2.setId(2);
        order3 = new Order(certificate, user, BigDecimal.valueOf(30.0), 3,
                LocalDateTime.parse("2021-03-22 09:22:11", formatter));
        order3.setId(3);

        when(orderDaoMock.findAll()).thenReturn(fullList);
        when(orderDaoMock.findById(1)).thenReturn(Optional.ofNullable(order1));
        //when(orderDaoMock.findByParameters(any(OrderFinder.class))).thenReturn(shortList);
        when(orderDaoMock.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(
                0, Order.class));
        when(orderDaoMock.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0, Order.class);
            order.setId(fullList.size());
            return order;
        });

        doNothing().when(validator).validate(any(Order.class));

        when(userServiceMock.getById(any(Integer.class))).thenReturn(user);
        when(certificateServiceMock.getById(any(Integer.class))).thenReturn(certificate);
    }

    @Test
    void getById_returnOrder() throws NotFoundException {
        assertEquals(order1, service.getById(1));
    }

    @Test
    void create_createOrder() throws ValidationException, BadRequestException {
        assertThrows(UnsupportedOperationException.class, () -> service.create(order1));
    }

    @Test
    void delete_deleteOrder() throws BadRequestException {
        service.delete(1);
        verify(orderDaoMock, atLeast(1)).findById(1);
    }

    @Test
    void update_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> service.update(order2));
    }

    @Test
    void find_badParameter_ThrowsBadRequestException() {
        OrderFinder finder = new OrderFinder();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("unknown", Collections.singletonList("1"));
        assertThrows(BadRequestException.class,
                () -> service.findByParameters(params, Pageable.unpaged()));
    }

    @Test
    void find_badParameterValue_ThrowsBadRequestException() {
        OrderFinder finder = new OrderFinder();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("limit", Collections.singletonList("a"));
        assertThrows(BadRequestException.class,
                () -> service.findByParameters(params, Pageable.unpaged()));
    }

    @Test
    void createOrder_createNewOrder()
            throws ValidationException, BadRequestException, NotFoundException {
        order1.setOrderAmount(order1.getCertificate().getPrice()
                .multiply(BigDecimal.valueOf(order1.getCertificateQuantity())));
        Order order2 = service.createOrder(order1.getCertificate().getId(),
                order1.getUser().getId(),
                order1.getCertificateQuantity());
        order1.setPurchaseDate(LocalDateTime.now());

        assertEquals(order1.getPurchaseDate().withNano(0), order2.getPurchaseDate().withNano(0));
    }
}
