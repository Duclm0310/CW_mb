import 'package:flutter/material.dart';
import 'package:appcl/models/Booking.dart';
import 'package:appcl/services/booking_service.dart';
import 'package:appcl/widgets/BookingItem.dart';
import 'Detail_Booking_Screen.dart';
import 'History_Screen.dart';

class BookingScreen extends StatefulWidget {
  const BookingScreen({super.key});

  @override
  State<BookingScreen> createState() => _BookingScreenState();
}

class _BookingScreenState extends State<BookingScreen> {
  List<Booking> listBookings = [];
  String errorMessage = "";
  bool isLoading = true;
  final BookingService _bookingService = BookingService();
  String? userIdCurrent;

  @override
  void initState() {
    super.initState();
    _loadClassInstances();
  }

  Future<void> _loadClassInstances() async {
    setState(() {
      isLoading = true;
      errorMessage = "";
    });
    try {
      Map<String, List<Booking>> bookings = await _bookingService.getUserBookingsbyStatus();
      setState(() {
        listBookings = bookings["bookingsPending"] ?? [];
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
        title: Text(
          "My Booking",
          style: TextStyle(fontSize: 25, fontWeight: FontWeight.bold),
        ),
      ),
      body: Padding(
        padding: EdgeInsets.all(10),
        child: Column(
          children: [
            if (errorMessage.isNotEmpty)
              Expanded(
                child: Center(
                  child: Text(
                    errorMessage,
                    style: TextStyle(color: Colors.red, fontSize: 18),
                    textAlign: TextAlign.center,
                  ),
                ),
              )
            else if (isLoading)
              Expanded(
                child: Center(
                  child: CircularProgressIndicator(),
                ),
              )
            else if (listBookings.isEmpty)
                Expanded(
                  child: Center(
                    child: Text(
                      "Booking empty",
                      style: TextStyle(fontSize: 18, color: Colors.grey),
                    ),
                  ),
                )
              else
                Expanded(
                  child: Container(
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(16),
                      color: Colors.grey.withOpacity(0.05),
                    ),
                    child: ListView.builder(
                      itemCount: listBookings.length,
                      padding: EdgeInsets.symmetric(vertical: 10),
                      itemBuilder: (context, index) {
                        final booking = listBookings[index];
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
                              _loadClassInstances();
                            });
                          },
                        );
                      },
                    ),
                  ),
                ),
            Container(
              padding: EdgeInsets.all(10),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text("Total ${listBookings.length} items"),
                  GestureDetector(
                    onTap: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(builder: (context) => HistoryScreen()),
                      );
                    },
                    child: const Text(
                      "History",
                      style: TextStyle(color: Colors.blue),
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(height: 10),
          ],
        ),
      ),
    );
  }
}
