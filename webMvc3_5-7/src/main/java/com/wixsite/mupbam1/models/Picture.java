package com.wixsite.mupbam1.models;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pictures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String url;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String owner_key;

   // @Column(name = "created_at", updatable = false)
   // @Temporal(TemporalType.TIMESTAMP)
   // @CreationTimestamp
   // private Date createdAt;
}

