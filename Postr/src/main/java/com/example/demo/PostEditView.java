package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class PostEditView extends VerticalLayout {

	// generated serialVersionUID
	private static final long serialVersionUID = 5583081102949090498L;

	// reference the repository of posts
	private final PostRepository repo;

	// reference the post being edited/created
	private Post post;

	// fields for the post
	TextField posterName = new TextField("Name");
	TextField postMessage = new TextField("Message");

	// buttons
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel", FontAwesome.TIMES_CIRCLE_O);
	Button delete = new Button("Delete", FontAwesome.TRASH_O);

	// CSS layout (for buttons)
	CssLayout actions = new CssLayout(save, cancel, delete);

	Binder<Post> binder = new Binder<>(Post.class);

	@Autowired
	public PostEditView(PostRepository repo) {
		this.repo = repo;

		// Adds the components in the given order to this component container.
		addComponents(posterName, postMessage, actions);

		// bind using naming convention
		binder.bindInstanceFields(this);

		// configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> repo.save(post));
		delete.addClickListener(e -> repo.delete(post));
		cancel.addClickListener(e -> editPost(post));
		setVisible(false);
	}

	// listen for changes...
	public interface ChangeHandler {
		void onChange();
	}

	public final void editPost(Post post) {
		// if there is not post to edit, make the component invisible
		if(post == null) {
			setVisible(false);
			return;
		}

		// store true if the post is not null, false otherwise
		final boolean isPersisted = post.getId() != null;

		if(isPersisted) {
			// find new entity to be edited
			post = repo.findOne(post.getId());
		}
		else {
			this.post = post;
		}

		// make cancel button visible if persisted, hide it otherwise
		cancel.setVisible(isPersisted);

		// bind customer properties to similarly named fields.
		binder.setBean(this.post);
		setVisible(true);

		// ensure the form is visible
		save.focus();

		// select all text in the "name" field automatically
		posterName.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}
}