package com.example.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactListAdapter extends RecyclerView.Adapter<ContactViewHolder> implements View.OnClickListener, Filterable {

    private Context context;
    private List<Contact> contactList;
    public List<Contact> filteredList;
    private LayoutInflater inflater;
    private int row_index = -1;
    private int clickCounter = 0;
    private String order;

    public ContactListAdapter(Context c, List<Contact> list, String order){
        this.context = c;
        this.contactList = list;
        this.inflater = LayoutInflater.from(context);
        this.order = order;
        this.filteredList = contactList;
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.contact_row, viewGroup, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder contactViewHolder, final int i) {
        Contact contact = filteredList.get(i);
        contactViewHolder.name.setText(contact.getName());
        contactViewHolder.surname.setText(contact.getSurname());
        contactViewHolder.card.setTag(i);
        contactViewHolder.layout.setTag(i);
        contactViewHolder.call.setTag(i);
        contactViewHolder.call.setOnClickListener(this);
        contactViewHolder.sendEmail.setTag(i);
        contactViewHolder.sendEmail.setOnClickListener(this);

        if(order.equals("surname")){
            contactViewHolder.name.setTypeface(contactViewHolder.name.getTypeface(), Typeface.NORMAL);
            contactViewHolder.surname.setTypeface(contactViewHolder.surname.getTypeface(), Typeface.BOLD);
        }else if(order.equals("name")){
            contactViewHolder.name.setTypeface(contactViewHolder.name.getTypeface(), Typeface.BOLD);
            contactViewHolder.surname.setTypeface(contactViewHolder.surname.getTypeface(), Typeface.NORMAL);
        }


        if(row_index == i){
            contactViewHolder.layout.setBackgroundColor(Color.parseColor("#E8E8E8"));
            contactViewHolder.call.setVisibility(View.VISIBLE);
            contactViewHolder.sendEmail.setVisibility(View.VISIBLE);
        }else{
            contactViewHolder.layout.setBackgroundColor(Color.WHITE);
            contactViewHolder.call.setVisibility(View.INVISIBLE);
            contactViewHolder.sendEmail.setVisibility(View.INVISIBLE);
        }
        contactViewHolder.layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout_card:
                if(clickCounter == 0) {
                    row_index = Integer.parseInt(v.getTag().toString());
                    clickCounter++;
                }else if(clickCounter == 1 && Integer.parseInt(v.getTag().toString()) == row_index){
                    contactPicker(v, row_index);
                }else if(clickCounter == 1 && Integer.parseInt(v.getTag().toString()) != row_index){
                    row_index = Integer.parseInt(v.getTag().toString());
                }
                notifyDataSetChanged();
                break;
            case R.id.card_phone:
                call(Integer.parseInt(v.getTag().toString()));
                break;
            case R.id.card_email:
                sendEmail(Integer.parseInt(v.getTag().toString()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void call(int i){
        String phone = filteredList.get(i).getPhone();
        if(phone != null) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + phone));
            context.startActivity(intent);
        }else{
            Toast.makeText(context, "Contact not callable - Missing phone number!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendEmail(int i){
        String email = filteredList.get(i).getEmail();
        if(email != null){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse("mailto:" + email));
            context.startActivity(emailIntent);
        }else{
            Toast.makeText(context, "Contact unreachable - Missing email address!", Toast.LENGTH_SHORT).show();
        }
    }

    public void contactPicker(View v, int i){
            Contact contact = filteredList.get(i);
            Intent intent = new Intent(context, ContactViewer.class);
            intent.putExtra("tag", i+"");
            intent.putExtra("id", contact.getId());
            intent.putExtra("surname", contact.getSurname());
            intent.putExtra("name", contact.getName());
            intent.putExtra("phone", contact.getPhone());
            intent.putExtra("email", contact.getEmail());
            intent.putExtra("imagePath", contact.getContactPic());
            clickCounter = 0;
            row_index = -1;
            context.startActivity(intent);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint!=null && constraint.length()>0){
                    List<Contact> tempList = new ArrayList<>();

                    for(Contact contact : contactList){
                        String name = contact.getName() + " " + contact.getSurname();
                            if ((name.toLowerCase().contains(constraint.toString().toLowerCase()))){
                                tempList.add(contact);
                            }
                    }
                    filterResults.count = tempList.size();
                    filterResults.values = tempList;
                }else{
                    //filterResults.count = contactList.size();
                    filterResults.values = contactList;
                }
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<Contact>)results.values;
                if(order.equals("surname"))
                    sortBySurname();
                if(order.equals("name"))
                    sortByName();

                notifyDataSetChanged();
            }
        };
    }

    public void sortByName(){
        Collections.sort(filteredList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                if(c1.getName() != null && c2.getName() != null)
                    return c1.getName().compareTo(c2.getName());
                else if(c1.getName() == null && c2.getName() == null )
                    return 0;
                else if(c1.getName() == null)
                    return -1;
                else
                    return 1;
            }
        });
    }

    public void sortBySurname(){
        Collections.sort(filteredList, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                if(c1.getSurname() != null && c2.getSurname() != null)
                    return c1.getSurname().compareTo(c2.getSurname());
                else if(c1.getSurname() == null && c2.getSurname() == null )
                    return 0;
                else if(c1.getSurname() == null)
                    return -1;
                else
                    return 1;
            }
        });
    }

}
