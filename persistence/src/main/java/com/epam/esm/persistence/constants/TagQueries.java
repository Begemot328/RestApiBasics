package com.epam.esm.persistence.constants;

public enum TagQueries {
   WHERE_ID (" WHERE id = ?"),
   WHERE_CERTIFICATE_ID (" WHERE certificate_id = ?"),
   SELECT_FROM_TAG("SELECT * FROM certificates.tag"),
   SELECT_FROM_TAG_CERTIFICATES("SELECT * FROM certificates.tag_certificates"),
   INSERT_TAG("INSERT INTO certificates.tag (name) VALUES (?);"),
   UPDATE_TAG("UPDATE certificates.tag  SET name = ? " +
                    "WHERE id = ?;"),
    DELETE_TAG("DELETE FROM certificates.tag  " +
                    "WHERE id = ?;");

    TagQueries(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
