package com.epam.esm.persistence.mapper;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.persistence.constants.CertificateColumns;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;

/**
 * Certificate to/from DB mapper.
 *
 * @author Yury Zmushko
 * @version 1.0
 */
@Component
public class CertificateMapper implements RowMapper<Certificate> {
    @Override
    public Certificate mapRow(ResultSet resultSet, int i) throws SQLException {
        Certificate certificate =  new Certificate(
                    resultSet.getString(CertificateColumns.NAME.getValue()),
                    resultSet.getBigDecimal(CertificateColumns.PRICE.getValue()),
                    resultSet.getInt(CertificateColumns.DURATION.getValue()));

        certificate.setCreateDate(resultSet.getTimestamp(CertificateColumns.CREATE_DATE.getValue())
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        certificate.setLastUpdateDate(resultSet.getTimestamp(CertificateColumns.LAST_UPDATE_DATE.getValue())
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        certificate.setDescription(resultSet.getString(CertificateColumns.DESCRIPTION.getValue()));
        certificate.setId(resultSet.getInt(CertificateColumns.ID.getValue()));
        return certificate;
    }
}
