package ru.mirea.petrov.mireaproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {

    interface ActionCallback {
        void onLongClickListener(Contact contact);
    }

    private Context context;
    private List<Contact> contactList;
    private ActionCallback mActionCallbacks;

    ContactRecyclerAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    void updateData(List<Contact> contacts) {
        this.contactList = contacts;
        notifyDataSetChanged();
    }

    //View Holder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private TextView mNameTextView;
        private TextView mPhoneTextView;

        ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this::onLongClick);

            mNameTextView = itemView.findViewById(R.id.nameTextView);
            mPhoneTextView = itemView.findViewById(R.id.phoneTextView);
        }

        void bindData(int position) {
            Contact contact = contactList.get(position);

            String fullName = contact.firstName + " " + contact.lastName;
            mNameTextView.setText(fullName);

            mPhoneTextView.setText(contact.getPhoneNumber());
        }

        @Override
        public boolean onLongClick(View v) {
            if (mActionCallbacks != null)
                mActionCallbacks.onLongClickListener(contactList.get(getAdapterPosition()));
            return true;
        }
    }

    void addActionCallback(ActionCallback actionCallbacks) {
        mActionCallbacks = actionCallbacks;
    }
}