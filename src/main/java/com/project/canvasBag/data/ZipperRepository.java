package com.project.canvasBag.data;

import com.project.canvasBag.model.Zipper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ZipperRepository extends PagingAndSortingRepository<Zipper, Long> {
    @Modifying
    @Query(value = "UPDATE Zipper SET name = ?2 WHERE id = ?1")
    void updateZipperName(Long id, String name);

    @Modifying
    @Query(value = "UPDATE Zipper SET comment = ?2 WHERE id = ?1")
    void updateZipperComment(Long id, String description);

    @Modifying
    @Query(value = "UPDATE Zipper SET remainder = ?2 WHERE id = ?1")
    void updateZipperRemainder(Long id, Integer remainder);

    @Modifying
    @Query(value = "UPDATE Zipper SET size = ?2 WHERE id = ?1")
    void updateZipperSize(Long id, Integer size);

    @Modifying
    @Query(value = "UPDATE Zipper SET color = ?2 WHERE id = ?1")
    void updateZipperColor(Long id, String color);

    @Modifying
    @Query(value = "UPDATE Zipper SET price = ?2 WHERE id = ?1")
    void updateZipperPrice(Long id, Double price);
}
