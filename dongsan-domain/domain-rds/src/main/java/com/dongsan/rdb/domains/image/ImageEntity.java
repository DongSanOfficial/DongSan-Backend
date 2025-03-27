package com.dongsan.rdb.domains.image;

import com.dongsan.core.domains.image.Image;
import com.dongsan.rdb.domains.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "image")
public class ImageEntity extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String url;

	protected ImageEntity() {
	}

	public ImageEntity(String url) {
		this.url = url;
	}

	public Image toImage() {
		return new Image(id, url);
	}

	public Long getId() {
		return id;
	}
}
