package com.example.muas.bukutamu.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.muas.bukutamu.R;
import com.example.muas.bukutamu.db.model.Contact;

import java.util.ArrayList;


public class dataAdapter extends ArrayAdapter<Contact> {

    Context context;
    ArrayList<Contact> mcontact;


    public dataAdapter(Context context, ArrayList<Contact> contact) {
        super(context, R.layout.listcontacts, contact);
        this.context = context;
        this.mcontact = contact;
    }

    public class Holder {
        TextView nameFV, alamatFV, instansiFV, nohpFV, tujuanFV, timeinFV;
        ImageView pic, picttd;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position

        Contact data = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        Holder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {


            viewHolder = new Holder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.listcontacts, parent, false);



            viewHolder.nameFV = (TextView) convertView.findViewById(R.id.txtViewer);
            viewHolder.alamatFV = (TextView) convertView.findViewById(R.id.txtAlamat);
            viewHolder.instansiFV = (TextView) convertView.findViewById(R.id.txtInstansi);
            viewHolder.nohpFV = (TextView) convertView.findViewById(R.id.txtNohp);
            viewHolder.tujuanFV = (TextView) convertView.findViewById(R.id.txtTujuan);
            viewHolder.timeinFV = (TextView) convertView.findViewById(R.id.txtTimein);
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.imgView);
            viewHolder.picttd = (ImageView) convertView.findViewById(R.id.imgViewttd);

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (Holder) convertView.getTag();
        }

        viewHolder.nameFV.setText("Nama tamu: " + data.getnama_model());
        viewHolder.alamatFV.setText("Alamat: " + data.getalamat_model());
        viewHolder.instansiFV.setText("Instansi: " + data.getinstansi_model());
        viewHolder.nohpFV.setText("No. HP: " + data.getnohp_model());
        viewHolder.tujuanFV.setText("Tujuan: " + data.gettujuan_model());
        viewHolder.timeinFV.setText("Waktu datang: " + data.getTimein_model());
        /*Picasso.with(context).load(contact.get_img(*/
        /*Picasso.with(context).load(String.valueOf(data.get_img())).into(viewHolder.pic);*/
        viewHolder.pic.setImageBitmap(convertToBitmap(data.get_img()));
        viewHolder.picttd.setImageBitmap(convertToBitmap(data.getSignature_model()));
        /*Picasso.with(context)
                .load(String.valueOf(data.get_img()))
                .resize(30, 30)
                .centerCrop()
                .placeholder(R.drawable.ic_home_black_24dp)
                .error(R.drawable.ic_dashboard_black_24dp)
                .into(viewHolder.pic);*/



        // Return the completed view to render on screen
        return convertView;
    }
    //get bitmap image from byte array

    private Bitmap convertToBitmap(byte[] b) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    }

}
