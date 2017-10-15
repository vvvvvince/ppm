package ch.viary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import ch.viary.domain.enumeration.PictureType;

import ch.viary.domain.enumeration.Visibility;

/**
 * A Picture.
 */
@Entity
@Table(name = "picture")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "picture")
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "data_content_type")
    private String dataContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private PictureType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility visibility;

    @Column(name = "shoot_date")
    private LocalDate shootDate;

    @Column(name = "post_date")
    private LocalDate postDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Picture raw;

    @OneToMany(mappedBy = "picture")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "picture")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Metadata> metadata = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "picture_album",
               joinColumns = @JoinColumn(name="pictures_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="albums_id", referencedColumnName="id"))
    private Set<Album> albums = new HashSet<>();

    @ManyToOne
    private Author author;

    @ManyToMany(mappedBy = "favorites")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Author> likers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Picture name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public Picture data(byte[] data) {
        this.data = data;
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return dataContentType;
    }

    public Picture dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public PictureType getType() {
        return type;
    }

    public Picture type(PictureType type) {
        this.type = type;
        return this;
    }

    public void setType(PictureType type) {
        this.type = type;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public Picture visibility(Visibility visibility) {
        this.visibility = visibility;
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public LocalDate getShootDate() {
        return shootDate;
    }

    public Picture shootDate(LocalDate shootDate) {
        this.shootDate = shootDate;
        return this;
    }

    public void setShootDate(LocalDate shootDate) {
        this.shootDate = shootDate;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public Picture postDate(LocalDate postDate) {
        this.postDate = postDate;
        return this;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public Picture getRaw() {
        return raw;
    }

    public Picture raw(Picture picture) {
        this.raw = picture;
        return this;
    }

    public void setRaw(Picture picture) {
        this.raw = picture;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Picture comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Picture addComment(Comment comment) {
        this.comments.add(comment);
        comment.setPicture(this);
        return this;
    }

    public Picture removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setPicture(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Metadata> getMetadata() {
        return metadata;
    }

    public Picture metadata(Set<Metadata> metadata) {
        this.metadata = metadata;
        return this;
    }

    public Picture addMetadata(Metadata metadata) {
        this.metadata.add(metadata);
        metadata.setPicture(this);
        return this;
    }

    public Picture removeMetadata(Metadata metadata) {
        this.metadata.remove(metadata);
        metadata.setPicture(null);
        return this;
    }

    public void setMetadata(Set<Metadata> metadata) {
        this.metadata = metadata;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public Picture albums(Set<Album> albums) {
        this.albums = albums;
        return this;
    }

    public Picture addAlbum(Album album) {
        this.albums.add(album);
        album.getPicures().add(this);
        return this;
    }

    public Picture removeAlbum(Album album) {
        this.albums.remove(album);
        album.getPicures().remove(this);
        return this;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public Author getAuthor() {
        return author;
    }

    public Picture author(Author author) {
        this.author = author;
        return this;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Set<Author> getLikers() {
        return likers;
    }

    public Picture likers(Set<Author> authors) {
        this.likers = authors;
        return this;
    }

    public Picture addLiker(Author author) {
        this.likers.add(author);
        author.getFavorites().add(this);
        return this;
    }

    public Picture removeLiker(Author author) {
        this.likers.remove(author);
        author.getFavorites().remove(this);
        return this;
    }

    public void setLikers(Set<Author> authors) {
        this.likers = authors;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Picture picture = (Picture) o;
        if (picture.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), picture.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Picture{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", data='" + getData() + "'" +
            ", dataContentType='" + dataContentType + "'" +
            ", type='" + getType() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", shootDate='" + getShootDate() + "'" +
            ", postDate='" + getPostDate() + "'" +
            "}";
    }
}
