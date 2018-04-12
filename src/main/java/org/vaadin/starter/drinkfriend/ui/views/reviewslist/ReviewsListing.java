package org.vaadin.starter.drinkfriend.ui.views.reviewslist;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.starter.drinkfriend.backend.Review;
import org.vaadin.starter.drinkfriend.backend.ReviewService;
import org.vaadin.starter.drinkfriend.ui.MainLayout;
import org.vaadin.starter.drinkfriend.ui.common.AbstractEditorDialog;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Review List")
public class ReviewsListing extends Div {

    private TextField search = new TextField();
    private Button addReview = new Button("Add review");
    private H2 header = new H2("");
    private VerticalLayout reviewsLayout = new VerticalLayout();
    private ReviewEditorDialog reviewForm = new ReviewEditorDialog(
        this::saveUpdate, this::deleteUpdate);

    public ReviewsListing() {
        add(header, search, addReview, reviewsLayout);
        reviewsLayout.setSizeFull();
        search.setPlaceholder("Search reviews");
        search.addValueChangeListener(e -> updateList());
        search.setValueChangeMode(ValueChangeMode.EAGER);

        addReview.addClickListener(e -> openForm(new Review(),
            AbstractEditorDialog.Operation.ADD));

        updateList();
    }

    public void saveUpdate(Review review,
        AbstractEditorDialog.Operation operation) {
        ReviewService.getInstance().saveReview(review);
        updateList();
        Notification.show(
            "Drink successfully " + operation.getNameInText() + "ed.",
            3000, Notification.Position.BOTTOM_START);
    }

    public void deleteUpdate(Review review) {
        ReviewService.getInstance().deleteReview(review);
        updateList();
        Notification.show("Drink successfully deleted.", 3000,
            Notification.Position.BOTTOM_START);
    }

    private void updateList() {
        List<Review> reviews = ReviewService.getInstance()
            .findReviews(search.getValue());
        if (search.isEmpty()) {
            header.setText("Reviews");
            header.add(new Span(", " + reviews.size() + " in total"));
        } else {
            header.setText("Search for “" + search.getValue() + "”");
            if (!reviews.isEmpty()) {
                header.add(new Span(reviews.size() + " results"));
            }
        }
        displayReviews(reviews);
    }

    private void displayReviews(List<Review> reviews) {
        reviewsLayout.removeAll();
        if (reviews == null || reviews.size() == 0) {
            Div div = new Div();
            div.setClassName("reviews__no-matches");
            div.setText("No matches");
            reviewsLayout.add(div);
            return;
        }
        for (Review review : reviews) {
            reviewsLayout.add(new ReviewItem(review, this));
        }
    }

    public void openForm(Review review,
        AbstractEditorDialog.Operation operation) {
        // Add the form lazily as the UI is not yet initialized when
        // this view is constructed
        if (reviewForm.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(reviewForm));
        }
        reviewForm.open(review, operation);
    }

}
