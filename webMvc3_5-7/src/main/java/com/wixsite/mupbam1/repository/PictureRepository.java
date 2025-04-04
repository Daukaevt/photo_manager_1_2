package com.wixsite.mupbam1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.wixsite.mupbam1.models.Picture;
import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {
    Page<Picture> findAll(Pageable pageable);

    @Query("SELECT p FROM Picture p WHERE p.owner_key = :ownerKey") // ✅ Исправлено: явно указываем поле
    Page<Picture> findByOwnerKey(@Param("ownerKey") String ownerKey, Pageable pageable);
}

