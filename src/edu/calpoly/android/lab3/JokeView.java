package edu.calpoly.android.lab3;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class JokeView extends LinearLayout implements OnClickListener, OnCheckedChangeListener, Checkable {

	private Button m_vwExpandButton;
	private RadioButton m_vwLikeButton;
	private RadioButton m_vwDislikeButton;
	private RadioGroup m_vwLikeGroup;
	private TextView m_vwJokeText;
	private Joke m_joke;

	public static final String EXPAND = " + ";
	public static final String COLLAPSE = " - ";

	/**
	 * Basic Constructor that takes only takes in an application Context.
	 * 
	 * @param context
	 *            The application Context in which this view is being added. 
	 *            
	 * @param joke
	 * 			  The Joke this view is responsible for displaying.
	 */
	public JokeView(Context context, Joke joke) {
		 super(context);
		 LayoutInflater inflater = (LayoutInflater)context.getSystemService(
				 Context.LAYOUT_INFLATER_SERVICE);

				 inflater.inflate(R.layout.joke_view, this, true);
				 
		m_vwExpandButton=(Button)this.findViewById(R.id.expandButton);
		m_vwLikeButton=(RadioButton)this.findViewById(R.id.likeButton);
		m_vwDislikeButton=(RadioButton)this.findViewById(R.id.dislikeButton);
	    m_vwLikeGroup=(RadioGroup)this.findViewById(R.id.ratingRadioGroup);
	    m_vwJokeText=(TextView)this.findViewById(R.id.jokeTextView);
	    
	    setJoke(joke);
	    collapseJokeView();
		 
	    m_vwExpandButton.setOnClickListener(this);
	    m_vwLikeGroup.setOnCheckedChangeListener(this);
	}

	/**
	 * Mutator method for changing the Joke object this View displays. This View
	 * will be updated to display the correct contents of the new Joke.
	 * 
	 * @param joke
	 *            The Joke object which this View will display.
	 */
	public void setJoke(Joke joke) {
		m_joke=joke;
		m_vwJokeText.setText(m_joke.getJoke());
		m_vwLikeGroup.clearCheck();
		if(m_joke.getRating()==Joke.LIKE)
			m_vwLikeButton.setChecked(true);
		if(m_joke.getRating()==Joke.DISLIKE)
			m_vwDislikeButton.setChecked(true);
	}

	/**
	 * This method encapsulates the logic necessary to update this view so that
	 * it displays itself in its "Expanded" form: 
	 * 	- Shows the complete text of the joke. 
	 *  - Brings the RadioGroup of rating Buttons back into view.
	 */
	private void expandJokeView() {
		m_vwJokeText.setMaxLines(10);
		m_vwJokeText.setEllipsize(null);
		m_vwExpandButton.setText(COLLAPSE);
		m_vwLikeGroup.setVisibility(VISIBLE);
		this.requestLayout();
	}

	/**
	 * This method encapsulates the logic necessary to update this view so that
	 * it displays itself in its "Collapsed" form: 
	 * 	- Shows only the first line of text of the joke. 
	 *  - If the joke is longer than one line, it appends an ellipsis to the end. 
	 *  - Removes the RadioGroup of rating Buttons from view.
	 */
	private void collapseJokeView() {
		m_vwJokeText.setMaxLines(2);
		m_vwJokeText.setEllipsize(TextUtils.TruncateAt.END);
		m_vwExpandButton.setText(EXPAND);
		m_vwLikeGroup.setVisibility(INVISIBLE);
		this.requestLayout();
		
	}

	@Override
	public void onClick(View v) {
		
		if(m_vwExpandButton.getText().equals(EXPAND))
			expandJokeView();
		else
			collapseJokeView();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int buttonId) {
		if(buttonId==R.id.likeButton)
			m_joke.setRating(Joke.LIKE);
		
		if(buttonId==R.id.dislikeButton)
			m_joke.setRating(Joke.DISLIKE);
		
	}

	@Override
	public boolean isChecked() {
		if(m_vwExpandButton.getText()== COLLAPSE)
			return true;
		else
			return false;
	}

	@Override
	public void setChecked(boolean checked) {
		if(checked)
			this.expandJokeView();
		else
			this.collapseJokeView();
		
	}

	@Override
	public void toggle() {
		setChecked(!isChecked());
	}

}
