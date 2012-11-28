package com.irccloud.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

public class NickservFragment extends DialogFragment {
    ServersDataSource.Server server;
    EditText pass;
    TextView nick;
	
    class SaveClickListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(pass.getText() != null && pass.getText().length() > 0) {
				NetworkConnection.getInstance().set_nspass(server.cid, pass.getText().toString());
				dismiss();
			}
		}
    }

    class InstructionsClickListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			NetworkConnection.getInstance().ns_help_register(server.cid);
			dismiss();
		}
    }

    public void setCid(int cid) {
    	server = ServersDataSource.getInstance().getServer(cid);
    	if(nick != null && server != null) {
	    	nick.setText("Password for " + server.nick);
	    	if(server.nickserv_pass != null)
	    		pass.setText(server.nickserv_pass);
    	}
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		Context ctx = getActivity();
		if(Build.VERSION.SDK_INT < 11)
			ctx = new ContextThemeWrapper(ctx, android.R.style.Theme_Dialog);
		LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	
    	View v = inflater.inflate(R.layout.dialog_nickserv,null);
    	nick = (TextView)v.findViewById(R.id.nickname);
    	pass = (EditText)v.findViewById(R.id.password);
    	if(server != null) {
	    	nick.setText("Password for " + server.nick);
	    	if(server.nickserv_pass != null)
	    		pass.setText(server.nickserv_pass);
    	}
    	
    	String title = "Identify your nickname on ";
    	if(server.name != null && server.name.length() > 0)
    		title += server.name;
    	else
    		title += server.hostname;
    	
    	AlertDialog d = new AlertDialog.Builder(ctx)
                .setTitle(title)
                .setView(v)
                .setPositiveButton("Save", new SaveClickListener())
                .setNeutralButton("Instructions", new InstructionsClickListener())
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
                })
                .create();
	    d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	    return d;
    }
}