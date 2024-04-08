package com.blogger.entities;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "blogs")
public class Blog {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "blog_id")
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "blog", columnDefinition = "TEXT")
	private String blog;

	@Temporal(TemporalType.DATE)
	@Column(name = "created_on", nullable = false, updatable = false)
	@CreationTimestamp
	private Date createdOn;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	@OneToMany(mappedBy = "blog", cascade = CascadeType.REMOVE)
	private List<Comment> comments;
}
