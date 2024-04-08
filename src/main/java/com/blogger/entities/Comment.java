package com.blogger.entities;

import java.sql.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "comments")
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "comment_id")
	private Long id;

	@Column(name = "comment", columnDefinition = "TEXT")
	private String comment;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on", nullable = false, updatable = false)
	@CreationTimestamp
	private Date createdOn;

	@ManyToOne
	@JoinColumn(name = "blog_id", referencedColumnName = "blog_id", nullable = false)
	private Blog blog;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;
}
