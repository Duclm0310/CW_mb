package com.example.cw_comp1786.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw_comp1786.R;
import com.example.cw_comp1786.db.entity.Booking;

import java.util.List;
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {
    private List<Booking> bookingList;
    private OnBookingClickListener listener;

    public BookingAdapter(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        holder.tvBookingDetails.setText(booking.getBookingId());
        holder.tvBookingEmail.setText(booking.getContactEmail());
        holder.tvBookingStatus.setText(booking.getStatus());

        holder.btnAccept.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAcceptClick(booking, position);
            }
        });
        holder.btnCancelled.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelClick(booking, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookingDetails, tvBookingEmail, tvBookingStatus;
        Button btnAccept, btnCancelled;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookingDetails = itemView.findViewById(R.id.tvBookingDetails);
            tvBookingEmail = itemView.findViewById(R.id.tvBookingEmail);
            tvBookingStatus = itemView.findViewById(R.id.tvBookingStatus);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnCancelled = itemView.findViewById(R.id.btnCancelled);
        }
    }

    public interface OnBookingClickListener {
        void onAcceptClick(Booking booking, int position);
        void onCancelClick(Booking booking, int position);
    }

    public void setOnItemClickListener(OnBookingClickListener listener) {
        this.listener = listener;
    }

}
