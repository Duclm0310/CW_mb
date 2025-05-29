import 'package:flutter/material.dart';
import 'package:appcl/models/Booking.dart';
import 'package:appcl/services/booking_service.dart';

import '../widgets/BookingItem.dart';
import 'Detail_Booking_Screen.dart';

class HistoryScreen extends StatefulWidget {
  const HistoryScreen({super.key});

  @override
  State<HistoryScreen> createState() => _HistoryScreenState();
}

class _HistoryScreenState extends State<HistoryScreen> {
  final BookingService _bookingService = BookingService();
  List<Booking> bookingsHistory = [];
  bool isLoading = true;
  String errorMessage = "";

  @override
  void initState() {
    super.initState();
    _loadHistoryBookings();
  }

  Future<void> _loadHistoryBookings() async {
    try {
      Map<String, List<Booking>> bookings = await _bookingService.getUserBookingsbyStatus();
      setState(() {
        bookingsHistory = bookings["bookingsHis"] ?? [];
        isLoading = false;
      });
    } catch (e) {
      setState(() {
        errorMessage = "Error: $e";
        isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          "History Booking",
          style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
        ),
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : errorMessage.isNotEmpty
          ? Center(child: Text(errorMessage))
          : bookingsHistory.isEmpty
          ? const Center(child: Text("No booking history found."))
          : ListView.builder(
        itemCount: bookingsHistory.length,
        itemBuilder: (context, index) {
          final booking = bookingsHistory[index];
          return BookingItem(
            booking: booking,
            onTap: () {
              Navigator.push(
                context,
                MaterialPageRoute(
                  builder: (context) =>
                      DetailBookingScreen(booking: booking),
                ),
              ).then((_) {
                _loadHistoryBookings();
              });
            },
          );
        },
      ),
    );
  }
}
