package it.UTeam.Onlineshopbot.entity;

import it.UTeam.Onlineshopbot.entity.templates.AbsEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "photos")
public class Photo extends AbsEntity {
    private UUID photoId;
}
