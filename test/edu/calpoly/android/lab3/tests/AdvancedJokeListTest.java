package edu.calpoly.android.lab3.tests;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import edu.calpoly.android.lab3.Joke;
import edu.calpoly.android.lab3.AdvancedJokeList;
import edu.calpoly.android.lab3.JokeView;

public class AdvancedJokeListTest extends ActivityInstrumentationTestCase2<AdvancedJokeList> {
	public AdvancedJokeListTest() {
		super("edu.calpoly.android.lab3", AdvancedJokeList.class);
	}

	@SmallTest
	public void testAddJokeViaButton() {
		ArrayList<Joke> m_arrJokeList = null;
		m_arrJokeList = this.retrieveHiddenMember("m_arrJokeList",m_arrJokeList,getActivity());
		EditText et = null;
		Button bt = null;
		final EditText m_vwJokeEditText = this.retrieveHiddenMember("m_vwJokeEditText",et,getActivity());
		final Button m_vwJokeButton = this.retrieveHiddenMember("m_vwJokeButton",bt,getActivity());
		assertEquals("Should be 3 default jokes",3,m_arrJokeList.size());
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				m_vwJokeEditText.setText("This is a test joke");
				m_vwJokeButton.performClick();
			}
			});
		// wait for the request to go through
		getInstrumentation().waitForIdleSync();
		assertEquals("Should be 4 jokes now",4,m_arrJokeList.size());
		assertEquals("Ensure the joke we added is really there","This is a test joke",m_arrJokeList.get(3).getJoke());	
		ListView m_vwJokeLayout = null;
		m_vwJokeLayout = this.retrieveHiddenMember("m_vwJokeLayout",m_vwJokeLayout,getActivity());
		assertEquals("Should be 4 joke views",4,m_vwJokeLayout.getChildCount());
		JokeView jv = (JokeView)m_vwJokeLayout.getChildAt(3);
		TextView tv = null;
		tv = this.retrieveHiddenMember("m_vwJokeText",tv,jv);	
		assertEquals("Text view should also have the new joke","This is a test joke",tv.getText());
	}
	
	@SmallTest
	public void testAddJokeViaReturn() {
		ArrayList<Joke> m_arrJokeList = null;
		m_arrJokeList = this.retrieveHiddenMember("m_arrJokeList",m_arrJokeList,getActivity());
		EditText et = null;
		final EditText m_vwJokeEditText = this.retrieveHiddenMember("m_vwJokeEditText",et,getActivity());
		assertEquals("Should be 3 default jokes",3,m_arrJokeList.size());
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				m_vwJokeEditText.setText("This is a second test joke");
			}
			});
		sendKeys(KeyEvent.KEYCODE_ENTER);
		assertEquals("Should be 4 jokes now",4,m_arrJokeList.size());
		assertEquals("Ensure the joke we added is really there","This is a second test joke",m_arrJokeList.get(3).getJoke());	
		ListView m_vwJokeLayout = null;
		m_vwJokeLayout = this.retrieveHiddenMember("m_vwJokeLayout",m_vwJokeLayout,getActivity());
		assertEquals("Should be 4 joke views",4,m_vwJokeLayout.getChildCount());
		JokeView jv = (JokeView)m_vwJokeLayout.getChildAt(3);
		TextView tv = null;
		tv = this.retrieveHiddenMember("m_vwJokeText",tv,jv);	
		assertEquals("Text view should also have the new joke","This is a second test joke",tv.getText());
	}

	@SmallTest
	public void testAddJokeViaDPadTrackBall() {
		ArrayList<Joke> m_arrJokeList = null;
		m_arrJokeList = this.retrieveHiddenMember("m_arrJokeList",m_arrJokeList,getActivity());
		EditText et = null;
		final EditText m_vwJokeEditText = this.retrieveHiddenMember("m_vwJokeEditText",et,getActivity());
		assertEquals("Should be 3 default jokes",3,m_arrJokeList.size());
		getActivity().runOnUiThread(new Runnable() {
			public void run() {
				m_vwJokeEditText.setText("This is a third test joke");
			}
			});
		sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		assertEquals("Should be 4 jokes now",4,m_arrJokeList.size());
		assertEquals("Ensure the joke we added is really there","This is a third test joke",m_arrJokeList.get(3).getJoke());	
		ListView m_vwJokeLayout = null;
		m_vwJokeLayout = this.retrieveHiddenMember("m_vwJokeLayout",m_vwJokeLayout,getActivity());
		assertEquals("Should be 4 joke views",4,m_vwJokeLayout.getChildCount());
		JokeView jv = (JokeView)m_vwJokeLayout.getChildAt(3);
		TextView tv = null;
		tv = this.retrieveHiddenMember("m_vwJokeText",tv,jv);	
		assertEquals("Text view should also have the new joke","This is a third test joke",tv.getText());
	}

	@SmallTest
	public void testAddJokeButtonText() {
		Button bt = null;
		Button m_vwJokeButton = this.retrieveHiddenMember("m_vwJokeButton",bt,getActivity());
		assertEquals("Check Button Text","Add Joke",m_vwJokeButton.getText());
	}

/*	@SmallTest
	public void testTextViewSize() {
		LinearLayout m_vwJokeLayout = null;
		m_vwJokeLayout = this.retrieveHiddenMember("m_vwJokeLayout",m_vwJokeLayout,getActivity());
		JokeView jv = (JokeView)m_vwJokeLayout.getChildAt(1);
		TextView tv = null;
		tv = this.retrieveHiddenMember("m_vwJokeText",tv,jv);	
		assertEquals("Check text size",16.0,tv.getTextSize(),.001);
	}*/

	/*************************************/
	/**	Java Friend-Class Helper Method **/
	/*************************************/
	@SuppressWarnings("unchecked")
	public <T> T retrieveHiddenMember(String memberName, T type, Object sourceObj) {
		Field field = null;
		T returnVal = null;
		//Test for proper existence
		try {
			field = sourceObj.getClass().getDeclaredField(memberName);
		} catch (NoSuchFieldException e) {
			fail("The field \"" + memberName + "\" was renamed or removed. Do not rename or remove this member variable.");
		}
		field.setAccessible(true);
		
		//Test for proper type
		try {
			returnVal = (T)field.get(sourceObj);
		} catch (ClassCastException exc) {
			fail("The field \"" + memberName + "\" had its type changed. Do not change the type on this member variable.");
		}  
		
		// Boiler Plate Exception Checking. If any of these Exceptions are 
		// throw it was becuase this method was called improperly.
		catch (IllegalArgumentException e) {
			fail ("This is an Error caused by the UnitTest!\n Improper user of retrieveHiddenMember(...) -- IllegalArgumentException:\n Passed in the wrong object to Field.get(...)");
		} catch (IllegalAccessException e) {
			fail ("This is an Error caused by the UnitTest!\n Improper user of retrieveHiddenMember(...) -- IllegalAccessException:\n Field.setAccessible(true) should be called.");
		}
		return returnVal; 
	}


}
