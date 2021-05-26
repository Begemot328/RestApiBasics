EXPLAIN
SELECT count(tag_id), name, t.id
FROM certificates.orders
         LEFT JOIN certificates.certificate_tag ct ON orders.certificate_id = ct.certificate_id
         LEFT JOIN certificates.tag t ON t.id = ct.tag_id
         JOIN
     /* derived2 */ (SELECT user_id, amount_sum
                     FROM
                         /* derived3 */ (SELECT user_id, sum(amount) AS amount_sum
                                         FROM certificates.orders
                                         GROUP BY user_id) AS t2
                     ORDER BY amount_sum DESC
                     LIMIT 1) AS t1 ON t1.user_id = orders.user_id
GROUP BY tag_id
ORDER BY COUNT(tag_id) DESC
LIMIT 1;