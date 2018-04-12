package org.vaadin.starter.drinkfriend.ui.views.reviewslist;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcons;
import org.vaadin.starter.drinkfriend.backend.Review;
import org.vaadin.starter.drinkfriend.ui.common.AbstractEditorDialog;

public class ReviewItem extends Div {

    public ReviewItem(Review review, ReviewsListing listing) {
        addClassName("view-container");
        addClassName("reviews");
        getStyle().set("width", "100%");
        getStyle().set("display", "flex");
        getStyle().set("padding-top", "2em");
        add(createRatingDiv(review));
        add(createDetailsDiv(review));
        add(createDateDiv(review));
        add(createButton(review, listing));

    }

    private Button createButton(Review review, ReviewsListing listing) {
        Button button = new Button("Edit");
        button.setIcon(VaadinIcons.EDIT.create());
        button.setClassName("review__edit");
        button.getElement().setAttribute("theme", "tertiary");
        button.addClickListener(e -> listing.openForm(review, AbstractEditorDialog.Operation.EDIT));
        return button;
    }

    private Div createDateDiv(Review review) {
        Div date = new Div();
        date.setClassName("review__date");
        H5 lastTasted = new H5("Last tasted");
        Paragraph dateP = new Paragraph(review.getDate().toString());
        date.add(lastTasted, dateP);
        return date;
    }

    private Div createRatingDiv(Review review) {
        Div ratingDiv = new Div();
        ratingDiv.addClassName("review__rating");
        Paragraph score = new Paragraph();
        score.setClassName("review__score");
        score.setText("" + review.getScore());
        Paragraph count = new Paragraph();
        count.setClassName("review__count");
        count.setText("" + review.getCount());
        Span timesTasted = new Span("times tasted");
        count.add(timesTasted);
        ratingDiv.add(score, count);
        return ratingDiv;
    }

    private Div createDetailsDiv(Review review) {
        Div detailsDiv = new Div();
        detailsDiv.addClassName("review__details");
        H4 h4 = new H4(review.getName());
        h4.addClassName("review__name");
        detailsDiv.add(h4);
        Paragraph category = new Paragraph();
        category.setClassName("review__category");
        if (review.getCategory() != null) {
            category.getElement().setAttribute("theme", "badge small");
            category.getElement().setAttribute("style","--category: " + review.getCategory().getId() + ";");
            category.setText(review.getCategory().getName());
        } else {
            category.getElement().setAttribute("style","--category: -1;");
            category.setText("Undefined");
        }
        detailsDiv.add(category);
        return detailsDiv;
    }


}
