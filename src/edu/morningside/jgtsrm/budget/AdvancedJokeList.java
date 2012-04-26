package edu.morningside.jgtsrm.budget;

import java.util.ArrayList;

import edu.morningside.jgtsrm.budget.Joke;
import edu.morningside.jgtsrm.budget.JokeListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

	public class AdvancedJokeList extends Activity {

		/**
		 * Contains the name of the Author for the jokes.
		 */
		protected String m_strAuthorName;

		/**
		 * Contains the list of Jokes the Activity will present to the user.
		 **/
		protected ArrayList<Joke> m_arrJokeList;
		protected ArrayList<Joke> m_arrFilteredJokeList;

		/**
		 * Adapter used to bind an AdapterView to List of Jokes.
		 */
		protected JokeListAdapter m_jokeAdapter;

		/**
		 * ViewGroup used for maintaining a list of Views that each display Jokes.
		 **/
		protected ListView m_vwJokeLayout;

		/**
		 * EditText used for entering text for a new Joke to be added to
		 * m_cursorJokeList.
		 **/
		protected EditText m_vwJokeEditText;

		/**
		 * Button used for creating and adding a new Joke to m_cursorJokeList using the
		 * text entered in m_vwJokeEditText.
		 **/
		protected Button m_vwJokeButton;
		
			
		/**
		 * Context-Menu MenuItem ID's
		 * IMPORTANT: You must use these when creating your MenuItems or the tests
		 * used to grade your submission will fail.
		 */
		protected static final int REMOVE_JOKE_MENUITEM = Menu.FIRST;
		protected static final int UPLOAD_JOKE_MENUITEM = Menu.FIRST + 1;

		/**
		 * Value used to filter which jokes get displayed to the user.
		 */
		protected int m_nFilter;

		/**
		 * Key used for storing and retrieving the value of m_nFilter in 
		 * savedInstanceState.
		 */
		protected static final String SAVED_FILTER_VALUE = "m_nFilter";

		/**
		 * Key used for storing and retrieving the text in m_vwJokeEditText in 
		 * savedInstanceState.
		 */
		protected static final String SAVED_EDIT_TEXT = "m_vwJokeEditText";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Initialize the ContentView
			initLayout();

			// Initialize Author Name
			Resources res = getResources();
			m_strAuthorName = res.getString(R.string.author_name);

			// Initialize the list of jokes from the strings.xml resource file
			m_nFilter = R.id.show_all_menuitem;
			m_arrJokeList = new ArrayList<Joke>();
			m_arrFilteredJokeList = new ArrayList<Joke>();

			m_jokeAdapter = new JokeListAdapter(this, m_arrFilteredJokeList);
			m_vwJokeLayout.setAdapter(m_jokeAdapter);
			m_vwJokeLayout.setOnItemLongClickListener(m_jokeAdapter);
			registerForContextMenu(m_vwJokeLayout);

			String[] resJokes = res.getStringArray(R.array.jokeList);
			for (int ndx = 0; ndx < resJokes.length; ndx++) {
				addJoke(new Joke(resJokes[ndx], m_strAuthorName));
			}

			// Initialize the "Add Joke" listeners
			initAddJokeListeners();
		}

		/**
		 * Method used to encapsulate the code that initializes and sets the Layout
		 * for this Activity.
		 */
		protected void initLayout() {
			setContentView(R.layout.advanced);
			m_vwJokeLayout = (ListView) findViewById(R.id.jokeListViewGroup);
			m_vwJokeLayout.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			m_vwJokeEditText = (EditText) findViewById(R.id.newJokeEditText);
			m_vwJokeButton = (Button) findViewById(R.id.addJokeButton);
		}

		/**
		 * Method used to encapsulate the code that initializes and sets the Event
		 * Listeners which will respond to requests to "Add" a new Joke to the list.
		 */
		protected void initAddJokeListeners() {
			m_vwJokeButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					String jokeText = m_vwJokeEditText.getText().toString(); 
					if (!jokeText.equals("")) {
						addJoke(new Joke(jokeText, m_strAuthorName));
						m_vwJokeEditText.setText("");
					}
				}
			});
			m_vwJokeEditText.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View view, int keyCode, KeyEvent event) {
					String jokeText = m_vwJokeEditText.getText().toString();
					if (!jokeText.equals("") && event.getAction() == KeyEvent.ACTION_DOWN && 
					 (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER)) { 
						addJoke(new Joke(jokeText ,m_strAuthorName));
						m_vwJokeEditText.setText("");
						return true;
					}
					if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(m_vwJokeEditText.getWindowToken(), 0);
						return true;
					}
					return false;
				}
			});
		}

		/**
		 * Method used for encapsulating the logic necessary to properly add a new
		 * Joke to m_arrJokeList, and display it on screen.
		 * 
		 * @param joke
		 *            The Joke to add to list of Jokes.
		 */
		protected void addJoke(Joke joke) {
			// Add joke
			m_arrJokeList.add(joke);
			m_arrFilteredJokeList.add(joke);
			m_jokeAdapter.notifyDataSetChanged();

			// Hide Soft Keyboard
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(m_vwJokeEditText.getWindowToken(), 0);
		}


		

		@Override
		public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
			super.onCreateContextMenu(menu, view, menuInfo);

			MenuItem item = menu.add(Menu.NONE, REMOVE_JOKE_MENUITEM, Menu.NONE, R.string.remove_menuitem);
			item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem arg0) {
					Joke joke = m_arrFilteredJokeList.remove(m_jokeAdapter
							.getSelectedPosition());
					m_arrJokeList.remove(joke);
					m_jokeAdapter.notifyDataSetChanged();
					return true;
				}
			});
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
		  super.onCreateOptionsMenu(menu);
		  getMenuInflater().inflate(R.menu.menu, menu);
		  menu.findItem(m_nFilter).setChecked(true);
		  return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		  int itemId = item.getItemId();
		  switch (itemId) {
		    case R.id.like_menuitem : 
		    case R.id.dislike_menuitem :  
		    case R.id.unrated_menuitem :  
		    case R.id.show_all_menuitem :  
		      setAndUpdateFilter(itemId);
		      return true;
				
		    default :
	              return super.onOptionsItemSelected(item);
		  } //end Switch
		} //end onOptionsItemSelected
		
		/**
		 * This method sets m_nFilter to the ID of the currently selected Filter 
		 * MenuItem and updates the m_cursorJokeList to contain only Joke Database 
		 * Table rows with ratings matching the desired filter value. 
		 * 
		 * This method should stop managing and close any unused Cursor objects.
		 * Additionally it should start managing any new Cursor objects and update
		 * the JokeCursorAdapter with any new Cursor objects. 
		 * 
		 * @param newFilterID
		 * 			  The ID of the filter MenuItem used to determine which jokes
		 * 			  get displayed. This should be one of four filter MenuItem ID 
		 * 			  values: R.id.like_menuitem, R.id.dislike_menuitem, 
		 * 			  R.id.unrated_menuitem, or R.id.show_all_menuitem.  
		 */
		protected void setAndUpdateFilter(int newFilterID) {
			// TODO	
			int filterVal = 0;
			int ndx = 0;
			Joke joke = null;
			
			// Set the filter value and Filter-SubMenu-Item checked
			m_nFilter = newFilterID;
			MenuItem filterItem = (MenuItem)findViewById(newFilterID);
			if (filterItem != null) {
				filterItem.setChecked(true);
			}

			// Set the filter value
			switch (m_nFilter) {
			case R.id.like_menuitem : 
				filterVal = Joke.LIKE;
				break;
			case R.id.dislike_menuitem : 
				filterVal = Joke.DISLIKE;
				break;
			case R.id.unrated_menuitem :  
				filterVal = Joke.UNRATED;
				break;
			case R.id.show_all_menuitem :  
			default :
				filterVal = Joke.LIKE;
				filterVal |= Joke.DISLIKE;
				filterVal |= Joke.UNRATED;
			}

			// Update the list of filtered Jokes
			m_arrFilteredJokeList.clear();
			for (ndx = 0; ndx < m_arrJokeList.size(); ndx++) {
				// If the rating on the Joke matches the filter, add it to the list.
				joke = m_arrJokeList.get(ndx);
				if ((joke.getRating() & filterVal) != 0) {
					m_arrFilteredJokeList.add(joke);
				}
			}
			m_jokeAdapter.notifyDataSetChanged();
		}
	}