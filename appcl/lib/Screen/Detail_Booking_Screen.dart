import 'package:appcl/services/booking_service.dart';
import 'package:flutter/material.dart';
import '../models/Booking.dart';

class DetailBookingScreen extends StatelessWidget {
  final Booking booking;

  const DetailBookingScreen({Key? key, required this.booking}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Booking Details"),
      ),
      body: Center(
        child: Column(
          mainAxisSize: MainAxisSize.min, // Đảm bảo nội dung không chiếm toàn bộ chiều cao
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text(
              "Title: ${booking.classTitle}",
              style: const TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 10),
            Text("Status: ${booking.status}"),
            const SizedBox(height: 10),
            if (booking.status == "Depending") ...[
              ElevatedButton(
                onPressed: () {
                  BookingService().deleteBooking(booking);
                  Navigator.pop(context);
                },
                child: const Text("Delete Booking"),
              ),
            ],
          ],
        ),
      ),
    );
  }
}
