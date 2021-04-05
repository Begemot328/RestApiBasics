package com.epam.esm.persistence.constants;

public enum TagQuerries {
   WHERE_ID (" WHERE id = ?"),
   WHERE_CERTIFICATE_ID (" WHERE certificate_id = ?"),
   READ_QUERY ("SELECT * FROM certificates.tag"),
   READ_BY_CERTIFICATE_QUERY ("SELECT * FROM certificates.tag_certificates"),
   INSERT_QUERY ("INSERT INTO certificates.tag (name) VALUES (?);"),
    UPDATE_QUERY ("UPDATE certificates.tag  SET name = ? " +
                    "WHERE id = ?;"),
    DELETE_QUERY ("DELETE FROM certificates.tag  " +
                    "WHERE id = ?;");

    TagQuerries(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
