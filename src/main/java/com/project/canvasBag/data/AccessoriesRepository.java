package com.project.canvasBag.data;

import com.project.canvasBag.model.Accessories;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface AccessoriesRepository extends PagingAndSortingRepository<Accessories, Long> {

    @Modifying
    @Query(value = "UPDATE Accessories SET name = ?2 WHERE id = ?1")
    void updateAccessoriesName(Long id, String name);

    @Modifying
    @Query(value = "UPDATE Accessories SET comment = ?2 WHERE id = ?1")
    void updateAccessoriesComment(Long id, String description);

    @Modifying
    @Query(value = "UPDATE Accessories SET remainder = ?2 WHERE id = ?1")
    void updateAccessoriesRemainder(Long id, Integer remainder);

    @Modifying
    @Query(value = "UPDATE Accessories SET size = ?2 WHERE id = ?1")
    void updateAccessoriesSize(Long id, String size);

    @Modifying
    @Query(value = "UPDATE Accessories SET material = ?2 WHERE id = ?1")
    void updateAccessoriesMaterial(Long id, String material);

    @Modifying
    @Query(value = "UPDATE Accessories SET price = ?2 WHERE id = ?1")
    void updateAccessoriesPrice(Long id, Double price);
}
