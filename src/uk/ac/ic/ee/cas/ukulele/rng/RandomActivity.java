package uk.ac.ic.ee.cas.ukulele.rng;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RandomActivity extends Activity {

	private ArrayList<String> m_numbers;
	private int currentIndex = 0;
	private TextView m_text;
	private TextView m_songName;
	private FileWriter m_output;
	private Boolean m_selected = false;
	private Boolean m_played = false;
	private Boolean m_showSelect = false;
	private String m_venue;
	
	private void setSong(int number)
	{
		m_text.setText(Integer.toString(number));
		m_songName.setText(getResources().getStringArray(R.array.songs)[number - 4]);
	}
	
	public RandomActivity()
	{
		
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == 0)
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Played?");
			builder.setCancelable(true);
			builder.setPositiveButton("Yes", new Dialog.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					m_played = true;
					dialog.dismiss();
					nextSong();
				}
			});
			builder.setNegativeButton("No", new Dialog.OnClickListener() {
	
				public void onClick(DialogInterface dialog, int which) {
					m_played = false;
					dialog.dismiss();
					nextSong();
				}
				
			});
			AlertDialog alertDialog = builder.create();
			
			
			return alertDialog;
		}
		else
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setTitle("Select Venue");
			builder.setItems(R.array.venues, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					m_venue = getResources().getStringArray(R.array.venues)[which];
				}
			});
			
			AlertDialog alertDialog = builder.create();
			return alertDialog;
		}
		
	}
	
	void nextSong()
	{
		if (m_showSelect)
		{
			Intent intent = new Intent(RandomActivity.this, SelectActivity.class);
			
			startActivityForResult(intent, 0);	
		}
		else
		{
			if (currentIndex < m_numbers.size())	{
				writeCurrent();
				m_selected = false;
				setSong(Integer.parseInt(m_numbers.get(++currentIndex)));
			}
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putStringArray("values", m_numbers.toArray(new String[m_numbers.size()]));
		outState.putInt("current", currentIndex);
		outState.putString("venue", m_venue);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (savedInstanceState != null)
		{
			currentIndex = savedInstanceState.getInt("current", 0);
			String[] vals = savedInstanceState.getStringArray("values");
			if (vals != null)
			{
				m_numbers = new ArrayList<String>(Arrays.asList(vals));
			}
			m_venue = savedInstanceState.getString("venue");
		}
		if (m_numbers == null)
		{
			m_numbers = new ArrayList<String>(215);
			for (int i = 0; i <= 211; ++i)
			{
				m_numbers.add(Integer.toString(i+4));
				
			}
			Collections.shuffle(m_numbers);
		}
		m_text = (TextView)findViewById(R.id.Number);
		m_songName = (TextView)findViewById(R.id.song_name);
		setSong(Integer.parseInt(m_numbers.get(currentIndex)));
		((Button) findViewById(R.id.button1)).setOnClickListener(mNextListener);
		((Button) findViewById(R.id.select_button)).setOnClickListener(mSelectListener);
		try
		{
			File d = new File(Environment.getExternalStorageDirectory(), "");
			File f = new File(d, "output.csv");
			if (!f.exists()) f.createNewFile();
			m_output = new FileWriter(f, true);
		}
		catch (IOException e)
		{
			m_output = null;
		}
		if (m_venue == null)
		{
			showDialog(1);
		}
	}
	
	OnClickListener mNextListener = new OnClickListener() {
		public void onClick(View v) {
			m_showSelect = false;
			showDialog(0);
		}
	};
	
	void writeCurrent()
	{
		try
		{
			if (m_output != null)
			{
				m_output.write(((String) DateFormat.format("yyyy,MM,dd,hh,mm,", new Date())) + (m_numbers.get(currentIndex)) + "," + m_selected + "," + m_played + "," + m_venue + "\n");
				m_output.flush();
			}
		}
		catch (IOException e)
		{
			
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (resultCode != -1)
		{
			writeCurrent();
			m_selected = true;
			String number = Integer.toString(resultCode);
			for (int i = 0; i < m_numbers.size(); ++i)
			{
				if (number.equals(m_numbers.get(i)))
				{
					// Swap the next number for the requested number
					m_numbers.set(i, m_numbers.get(currentIndex + 1));
					m_numbers.set(currentIndex + 1, number);
					break;
				}
			}
			if (currentIndex < m_numbers.size())	{
				setSong(Integer.parseInt(m_numbers.get(++currentIndex)));
			}
		}
	}
	
	OnClickListener mSelectListener = new OnClickListener() {
		
		public void onClick(View v) {
			m_showSelect = true;
			showDialog(0);
		}
	};
}
