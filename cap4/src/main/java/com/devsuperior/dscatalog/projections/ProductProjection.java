package com.devsuperior.dscatalog.projections;

public interface ProductProjection {

  Long getId();

  String getName();
  /*

  Projection Usage to reflect the data returned from DB as specified below

  SELECT DISTINCT tb_product.id, tb_product.name
  FROM tb_product
  INNER JOIN tb_product_category ON tb_product.id = tb_product_category.product_id
  WHERE tb_product_category.category_id IN (1,3)
  AND LOWER(tb_product.name) LIKE '%ma%'
  ORDER BY  tb_product.name

  ID  	NAME
  3	    Macbook Pro
  16	  PC Gamer Max
  2	    Smart TV

*/
}
