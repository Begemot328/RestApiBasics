package com.epam.esm.persistence.constants;

public enum CertificateQueries {
    SELECT_FROM_CERTIFICATE_TAG("SELECT * FROM certificates.certificate_tags"),
    WHERE_TAG_ID(" WHERE tag_id = ?"),
    WHERE_ID(" WHERE id = ?"),
    SELECT_FROM_CERTIFICATE("SELECT * FROM certificates.certificate"),
    INSERT_CERTIFICATE("INSERT INTO certificates.certificate (name, description, price," +
            "duration, create_date, last_update_date) VALUES (?,?,?,?,?,?);"),
    UPDATE_CERTIFICATE("UPDATE certificates.certificate SET name=?, description=?, price=?," +
            "duration=?, create_date=?, last_update_date=? " +
            "WHERE id = ?;"),
    DELETE_CERTIFICATE("DELETE FROM certificates.certificate  " +
            "WHERE id = ?;"),
    ADD_CERTIFICATE_TAG("INSERT INTO certificates.certificate_tag " +
            "(certificate_id, tag_id) VALUES (?,?);"),
    DELETE_CERTIFICATE_TAG("DELETE FROM certificates.certificate_tag " +
            "WHERE certificate_id=? AND tag_id=?;"),
    COUNT_CERTIFICATE_TAG("SELECT COUNT(*) FROM certificates.certificate_tag " +
            "WHERE tag_id = ? AND certificate_id = ?;");

    CertificateQueries(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
