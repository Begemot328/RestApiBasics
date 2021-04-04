package com.epam.esm.persistence.constants;

public enum CertificateQuerries {
    READ_BY_TAG_QUERY("SELECT * FROM certificate_tags"),
    WHERE_TAG_ID(" WHERE tag_id = ?"),
    WHERE_ID(" WHERE id = ?"),
    READ_QUERY("SELECT * FROM certificates.certificate"),
    INSERT_QUERY("INSERT INTO certificates.certificate (name, description, price," +
            "duration, create_date, last_update_date) VALUES (?,?,?,?,?,?);"),
    UPDATE_QUERY("UPDATE certificates.certificate SET name=?, description=?, price=?," +
            "duration=?, create_date=?, last_update_date=? " +
            "WHERE id = ?;"),
    DELETE_QUERY("DELETE FROM certificates.certificate  " +
            "WHERE id = ?;"),
    ADD_CERTIFICATE_TAG("INSERT INTO certificates.certificate_tag " +
            "(certificate_id, tag_id) VALUES (?,?);"),
    DELETE_CERTIFICATE_TAG("DELETE FROM certificates.certificate_tag " +
            "WHERE certificate_id=? AND tag_id=?;"),
    CHECK_RELATION_BY_TAG("SELECT COUNT(*) FROM certificate_tag " +
            "WHERE tag_id = "),
    CHECK_RELATION_BY_CERTIFICATE(" AND certificate_id = ");

    CertificateQuerries(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
