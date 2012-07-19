package uk.ac.ic.ee.cas.ukulele.rng;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SelectActivity extends Activity {

	class NumberButtonListener implements OnClickListener
	{
		public NumberButtonListener(int num) {m_number = num;}


	public void onClick(View v) {
		// TODO Auto-generated method stub
		m_value += Integer.toString(m_number);
		updateName();
	}
	private int m_number;
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        
        ((Button)findViewById(R.id.button1)).setOnClickListener(new NumberButtonListener(1));
        ((Button)findViewById(R.id.button2)).setOnClickListener(new NumberButtonListener(2));
        ((Button)findViewById(R.id.button3)).setOnClickListener(new NumberButtonListener(3));
        ((Button)findViewById(R.id.button4)).setOnClickListener(new NumberButtonListener(4));
        ((Button)findViewById(R.id.button5)).setOnClickListener(new NumberButtonListener(5));
        ((Button)findViewById(R.id.button6)).setOnClickListener(new NumberButtonListener(6));
        ((Button)findViewById(R.id.button7)).setOnClickListener(new NumberButtonListener(7));
        ((Button)findViewById(R.id.button8)).setOnClickListener(new NumberButtonListener(8));
        ((Button)findViewById(R.id.button9)).setOnClickListener(new NumberButtonListener(9));
        ((Button)findViewById(R.id.button10)).setOnClickListener(new NumberButtonListener(0));
        ((Button)findViewById(R.id.back_button)).setOnClickListener(m_backListener);
        ((Button)findViewById(R.id.select_button)).setOnClickListener(m_selectListener);
        m_songInfo = (TextView)findViewById(R.id.song_info);
        updateName();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_select, menu);
        return true;
    }

    void updateName()
    {
    	if (m_value.length() == 0) {
    		m_songInfo.setText("");
    	}
    	else
    	{
    		int song = Integer.parseInt(m_value);
    		if (song >= 4 && song <= 215)
    		{
    			m_songInfo.setText(m_value + ": " + getResources().getStringArray(R.array.songs)[song - 4]);
    		}
    		else
    		{
    			m_songInfo.setText(m_value + ": Invalid Song");
    		}
    	}
    }
    
    private TextView m_songInfo;
    private String m_value = "";
    private OnClickListener m_backListener = new OnClickListener() {
		
		public void onClick(View v) {
			if (m_value.length() > 0)
			{
				m_value = m_value.substring(0, m_value.length() - 1);
				updateName();
			}
			else
			{
				// GO BACK
				setResult(-1);
				finish();
			}
		}
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
	}
	private OnClickListener m_selectListener = new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setResult(-1);
			if (m_value.length() > 0)
			{
				int song = Integer.parseInt(m_value);
				if (song >= 4 && song <= 215) setResult(song);
			}
			finish();
		}
	};
}
