package com.saude360.backendsaude360.entities;
import com.saude360.backendsaude360.entities.users.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orientation_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrientationResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private List<String> filePath = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "orientation_id")
    private Orientation orientation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;
}
