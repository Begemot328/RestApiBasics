package test.com.epam.esm.service.service;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.model.entity.User;
import com.epam.esm.persistence.dao.order.OrderDAOImpl;
import com.epam.esm.persistence.dao.tag.TagDAOImpl;
import com.epam.esm.persistence.util.finder.EntityFinder;
import com.epam.esm.persistence.util.finder.impl.OrderFinder;
import com.epam.esm.service.constants.PaginationParameters;
import com.epam.esm.service.exceptions.BadRequestException;
import com.epam.esm.service.exceptions.NotFoundException;
import com.epam.esm.service.exceptions.ValidationException;
import com.epam.esm.service.service.certificate.CertificateService;
import com.epam.esm.service.service.order.OrderServiceImpl;
import com.epam.esm.service.service.user.UserService;
import com.epam.esm.service.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    @PersistenceContext
    private EntityManager entityManager;

    @MockBean
    OrderDAOImpl orderDaoMock;

    @MockBean
    TagDAOImpl tagDaoMock;

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
    void init() throws ValidationException {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> tagRoot = query.from(Tag.class);
        query = query.select(tagRoot);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Certificate certificate = new Certificate("nastolka.by",
                BigDecimal.valueOf(40.1), 50);
        certificate.setDescription("board games certificate");
        certificate.setCreateDate(LocalDateTime.parse("2021-01-22 09:20:11", formatter));
        certificate.setLastUpdateDate(LocalDateTime.parse("2021-03-22 09:20:11", formatter));
        certificate.setId(2);

        User user = new User("Ivan", "Ivanov", "Ivanov", "qwerty");
        user.setId(2);

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
        when(orderDaoMock.getById(1)).thenReturn(order1);
        when(orderDaoMock.findByParameters(any(OrderFinder.class))).thenReturn(shortList);
        when(orderDaoMock.update(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(
                0, Order.class));
        when(orderDaoMock.getBuilder()).thenReturn(builder);
        when(orderDaoMock.create(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0, Order.class);
            order.setId(fullList.size());
            return order;
        });

        doNothing().when(validator).validate(any(Order.class));

        when(userServiceMock.readOptional(any(Integer.class))).thenReturn(Optional.of(user));
        when(certificateServiceMock.readOptional(any(Integer.class))).thenReturn(Optional.of(certificate));
    }

    @Test
    void read_returnOrder() throws NotFoundException {
        assertEquals(order1, service.read(1));
    }

    @Test
    void readAll_returnOrders() throws NotFoundException {
        assertEquals(fullList, service.readAll());
    }

    @Test
    void create_createOrder() throws ValidationException, BadRequestException {
        Order order = service.create(order1);
        assertEquals(order.getId(), fullList.size());
        verify(orderDaoMock, atLeast(1)).create(order1);
    }

    @Test
    void delete_deleteOrder() throws BadRequestException {
        service.delete(1);
        verify(orderDaoMock, atLeast(1)).getById(1);
    }

    @Test
    void update_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> service.update(order2));
    }

    @Test
    void read_parsePaginationLimit_invokeFinderLimit()
            throws NotFoundException, BadRequestException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.LIMIT.getParameterName(), Collections.singletonList("1"));
        service.read(params);

        ArgumentCaptor<EntityFinder<Order>> captor = ArgumentCaptor.forClass(EntityFinder.class);
        verify(orderDaoMock, atLeast(1)).findByParameters(captor.capture());
        assertEquals(1, captor.getValue().getLimit());
    }

    @Test
    void read_parsePaginationLimit_invokeFinderOffset() throws NotFoundException, BadRequestException {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put(PaginationParameters.OFFSET.getParameterName(), Collections.singletonList("1"));
        service.read(params);

        ArgumentCaptor<EntityFinder<Order>> captor = ArgumentCaptor.forClass(EntityFinder.class);
        verify(orderDaoMock, atLeast(1)).findByParameters(captor.capture());
        assertEquals(1, captor.getValue().getOffset());
    }

    @Test
    void read_badParameter_ThrowsBadRequestException() {
        OrderFinder finder = new OrderFinder(orderDaoMock);
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("unknown", Collections.singletonList("1"));
        assertThrows(BadRequestException.class, () -> service.read(params));
    }

    @Test
    void read_badParameterValue_ThrowsBadRequestException() {
        OrderFinder finder = new OrderFinder(orderDaoMock);
        finder.offset(1);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() {
        };
        params.put("limit", Collections.singletonList("a"));
        assertThrows(BadRequestException.class, () -> service.read(params));
    }

    @Test
    void createOrder_createNewOrder() throws ValidationException, BadRequestException {
        order1.setOrderAmount(order1.getCertificate().getPrice()
                .multiply(BigDecimal.valueOf(order1.getCertificateQuantity())));
        Order order2 = service.createOrder(order1.getCertificate(),
                order1.getUser(),
                order1.getCertificateQuantity());
        order1.setPurchaseDate(LocalDateTime.now());

        assertEquals(order1.getPurchaseDate().withNano(0), order2.getPurchaseDate().withNano(0));
    }
}
