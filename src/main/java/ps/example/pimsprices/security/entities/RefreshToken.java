package ps.example.pimsprices.security.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@EqualsAndHashCode
@Data
@Entity(name = "RefreshToken")
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;

    public RefreshToken() {
    }

}





























//
//import jakarta.persistence.*;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//
//@Data
//@Entity
//@Table(name = "refresh_tokens")
//public class RefreshToken {
//    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    @Column(unique = true, nullable = false)
//    private String token;
//
////    @Column(nullable = false) //u mnie
////    private LocalDateTime expirationTime;
//
////    @Column(nullable = false) //u bezkoder
////    private Instant expiryDate;
//
//    @Column(nullable = false) // teraz
//    private LocalDateTime expirationTime;
//
//    @OneToOne
//    @JoinColumn(name = "user_id")
////    @JoinColumn(name = "user_id", referencedColumnName = "id") //u bezkoder
//    private User user;
//
//
//    public RefreshToken() {
//    }
//
//
//
//}
