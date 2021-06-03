package com.elmenus.order.basket.checkout.model;

import com.elmenus.order.basket.checkout.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="userId",referencedColumnName=Constants.ID_FIELD)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    // Boolean flag to indicate if the basket has already been check out
    private boolean checkedOut;
}
