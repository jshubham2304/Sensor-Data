package com.nitinsir.sensordata;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import static com.nitinsir.sensordata.MainActivity.mylist;

class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.TaskViewHolder> {

// Class variables for the Cursor that holds task data and the Context
private Cursor mCursor;
private Context mContext;


/**
 * Constructor for the CustomCursorAdapter that initializes the Context.
 *
 * @param mContext the current Context
 */
public CustomCursorAdapter(Context mContext) {
        this.mContext = mContext;
        }


/**
 * Called when ViewHolders are created to fill a RecyclerView.
 *
 * @return A new TaskViewHolder that holds the view for each task
 */
@Override
public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
        .inflate(R.layout.task_layout, parent, false);

        return new TaskViewHolder(view);
        }


/**
 * Called by the RecyclerView to display data at a specified position in the Cursor.
 *
 * @param holder The ViewHolder to bind Cursor data to
 * @param position The position of the data in the Cursor
 */
@Override
public void onBindViewHolder(TaskViewHolder holder, int position) {

        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(TaskContract.TaskEntry._ID);
        int statementIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_STATEMENT);
        int RIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_R);
    int GIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_G);
    int BIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_B);
    int ImageIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_IMAGEPATH);

        mCursor.moveToPosition(position); // get to the right location in the cursor

// Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String statement = mCursor.getString(statementIndex);
    int r = mCursor.getInt(RIndex);
    int g = mCursor.getInt(GIndex);
    int b = mCursor.getInt(BIndex);
    String imagePath = mCursor.getString(ImageIndex);
      //  mylist.add(new DataStorage(statement,r,g,b,imagePath));
    //Set values
        holder.itemView.setTag(id);
        holder.statement.setText(statement);
    holder._R.setText("R -" +r);
    holder._G.setText("G -"+g);
    holder._B.setText("B -"+b);
    holder.img.setImageURI(Uri.parse(imagePath));

        }

@Override
public int getItemCount() {
        if (mCursor == null) {
        return 0;
        }
        return mCursor.getCount();
        }


public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
        return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
        this.notifyDataSetChanged();
        }
        return temp;
        }


// Inner class for creating ViewHolders
class TaskViewHolder extends RecyclerView.ViewHolder {

    // Class variables for the task description and priority TextViews
    TextView statement;
    TextView _R;
    TextView _G;
    TextView _B;
    ImageView img;

    /**
     * Constructor for the TaskViewHolders.
     *
     * @param itemView The view inflated in onCreateViewHolder
     */
    public TaskViewHolder(View itemView) {
        super(itemView);
        statement =(TextView)itemView.findViewById(R.id.statement);
        _R =(TextView) itemView.findViewById(R.id.r);
        _G =(TextView) itemView.findViewById(R.id.g);
        _B =(TextView) itemView.findViewById(R.id.b);
        img =(ImageView) itemView.findViewById(R.id.img);

    }
}
}
