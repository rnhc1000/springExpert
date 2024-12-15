package com.devsuperior.demo.entities;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private LocalDate date;
	private String url;

	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;

//	@Column(name = "city_id", nullable = false, insertable = false, updatable = false)
//	private Long  cityId;

	public Event() {
  }

	public Event(Long id) {
		this.id = id;
	}

	public Event(Long id, String name, LocalDate date, String url) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.url = url;
  }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCityName() {
		return city.getName();
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Event{" +
					 "id=" + id +
					 ", name='" + name + '\'' +
					 ", date=" + date +
					 ", url='" + url + '\'' +
					 ", city=" + city +
					 '}';
	}
}
