package com.project.canvasBag.data;

import com.project.canvasBag.model.Tape;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface TapeRepository extends PagingAndSortingRepository<Tape, Long> {

    @Modifying
    @Query(value = "UPDATE Tape SET name = ?2 WHERE id = ?1")
    void updateTapeName(Long id, String name);

    @Modifying
    @Query(value = "UPDATE Tape SET comment = ?2 WHERE id = ?1")
    void updateTapeComment(Long id, String description);

    @Modifying
    @Query(value = "UPDATE Tape SET remainder = ?2 WHERE id = ?1")
    void updateTapeRemainder(Long id, Double remainder);

    @Modifying
    @Query(value = "UPDATE Tape SET width = ?2 WHERE id = ?1")
    void updateTapeWidth(Long id, Integer width);

    @Modifying
    @Query(value = "UPDATE Tape SET color = ?2 WHERE id = ?1")
    void updateTapeColor(Long id, String color);

    @Modifying
    @Query(value = "UPDATE Tape SET price = ?2 WHERE id = ?1")
    void updateTapePrice(Long id, Double price);
}
