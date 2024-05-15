package com.project.canvasBag.data;

import com.project.canvasBag.model.Fabric;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;

@Transactional
public interface FabricRepository extends PagingAndSortingRepository<Fabric, Long> {

    @Modifying
    @Query(value = "UPDATE Fabric SET name = ?2 WHERE id = ?1")
    void updateFabricName(Long id, String name);

    @Modifying
    @Query(value = "UPDATE Fabric SET color = ?2 WHERE id = ?1")
    void updateFabricColor(Long id, String color);

    @Modifying
    @Query(value = "UPDATE Fabric SET remainder = ?2 WHERE id = ?1")
    void updateFabricRemainder(Long id, Double remainder);

    @Modifying
    @Query(value = "UPDATE Fabric SET thickness = ?2 WHERE id = ?1")
    void updateFabricThickness(Long id, Integer thickness);

    @Modifying
    @Query(value = "UPDATE Fabric SET price = ?2 WHERE id = ?1")
    void updateFabricPrice(Long id, Double price);

    @Modifying
    @Query(value = "UPDATE Fabric SET comment = ?2 WHERE id = ?1")
    void updateFabricComment(Long id, String comment);

    @Modifying
    @Query(value = "UPDATE Fabric SET nameFile = ?2 WHERE id = ?1")
    void updateFabricFileName(Long id, String nameFile);

    @Query(value = "SELECT nameFile FROM Fabric WHERE id = ?1")
    String getNameFile(Long id);
}
