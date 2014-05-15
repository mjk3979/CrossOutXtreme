package com.crossoutxtrem;

import com.crossoutxtrem.R;
import java.io.InputStreamReader;
import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;

public class About extends Activity
{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
		loadLicense();
    }

	private void loadLicense()
	{
		StringBuilder sb = new StringBuilder(1024);
		InputStreamReader reader = null;
		try
		{
			reader = new InputStreamReader(getResources().openRawResource(R.raw.license));
			while(reader.ready())
				sb.append((char)reader.read());
		}
		catch (Exception ex) {}
		finally
		{
			try
			{
				if (reader != null)
					reader.close();
			}
			catch (Exception ex) {}
		}
		TextView licenseView = (TextView) findViewById(R.id.textView2);
		licenseView.setText(sb.toString());
	}
}
