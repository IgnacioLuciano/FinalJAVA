package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SpringUI
public class PostView extends UI{

	private static final long serialVersionUID = 566375499481285690L;

	private final PostRepository repo;

	private final PostEditView postEditView;

	final Grid<Post> grid;

	final TextField filter;

	private final Button addNewPostButton;

	@Autowired
	public PostView(PostRepository repo, PostEditView postEditView) {
		this.repo = repo;
		this.postEditView = postEditView;
		this.grid = new Grid<>(Post.class);
		this.filter = new TextField();
		this.addNewPostButton = new Button("New Post", FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest request) {
	
		HorizontalLayout actions = new HorizontalLayout(filter, addNewPostButton);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, postEditView);
		setContent(mainLayout);

		grid.setHeight(300, Unit.PIXELS);
		grid.setColumns("id", "firstName", "lastName");

		filter.setPlaceholder("Filter by last name");

		filter.setValueChangeMode(ValueChangeMode.LAZY);
		filter.addValueChangeListener(e -> listPosts(e.getValue()));

		grid.asSingleSelect().addValueChangeListener(e -> {
			postEditView.editPost(e.getValue());
		});

	
		addNewPostButton.addClickListener(e -> postEditView.editPost(new Post("", "")));


		postEditView.setChangeHandler(() -> {
			postEditView.setVisible(false);
			listPosts(filter.getValue());
		});

		listPosts(null);
	}

	void listPosts(String filterText) {
		if (StringUtils.isEmpty(filterText)) {
			grid.setItems(repo.findAll());
		}
		else {
			grid.setItems(repo.findByPosterName(filterText));
		}
	}
}