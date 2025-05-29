

import 'package:appcl/models/Booking.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:shared_preferences/shared_preferences.dart';

class BookingService{
  final DatabaseReference _bookingRef = FirebaseDatabase.instance.ref("bookings");
  final DatabaseReference _classInstanceRef = FirebaseDatabase.instance.ref("classInstances");
  final DatabaseReference _userRef = FirebaseDatabase.instance.ref("users");
  final DatabaseReference _classRef = FirebaseDatabase.instance.ref("yoga_classes");

  Future<List<Booking>> getUserBookings() async {
    try {
      Map<String, String?> userData = await getUserData();
      String? currentUserId = userData['userId'];
      if (currentUserId == null) return [];

      List<Booking> bookings = [];

      Query userBookingsQuery = _bookingRef.orderByChild("userId").equalTo(currentUserId);
      DatabaseEvent userBookingsEvent = await userBookingsQuery.once();

      if (userBookingsEvent.snapshot.exists) {
        for (var child in userBookingsEvent.snapshot.children) {
          String instanceId = child.child('instanceId').value.toString();

          var info = await getClassAndUserInfo(instanceId);
          bookings.add(Booking.fromSnapshot(child, info['classTitle']!, info['userName']!));
        }
      }

      return bookings;
    } catch (e) {
      throw Exception("Error fetching user bookings: $e");
    }
  }


  Future<Map<String, List<Booking>>> getUserBookingsbyStatus() async {
    try {
      Map<String, String?> userData = await getUserData();
      String? currentUserId = userData['userId'];
      if (currentUserId == null) return {} ;

      List<Booking> bookings_pending = [];
      List<Booking> bookings_his = [];

      Query userBookingsQuery = _bookingRef.orderByChild("userId").equalTo(currentUserId);
      DatabaseEvent userBookingsEvent = await userBookingsQuery.once();

      if (userBookingsEvent.snapshot.exists) {
        for (var child in userBookingsEvent.snapshot.children) {
          if(child.child('status').value.toString() == "Pending")
          {
            String instanceId = child.child('instanceId').value.toString();
            var info = await getClassAndUserInfo(instanceId);
            bookings_pending.add(Booking.fromSnapshot(child, info['classTitle']!, info['userName']!));
          }else{
            String instanceId = child.child('instanceId').value.toString();
            var info = await getClassAndUserInfo(instanceId);
            bookings_his.add(Booking.fromSnapshot(child, info['classTitle']!, info['userName']!));
          }
        }
      }
      return {"bookingsPending": bookings_pending, "bookingsHis": bookings_his};
    } catch (e) {
      throw Exception("Error fetching user bookings: $e");
    }
  }



  Future<Map<String, String>> getClassAndUserInfo(String instanceId) async {
    DatabaseEvent instanceEvent = await _classInstanceRef.child(instanceId).once();
    String classId = instanceEvent.snapshot.child('classId').value.toString();
    String userId = instanceEvent.snapshot.child('userId').value.toString();

    DatabaseEvent classEvent = await _classRef.child(classId).once();
    String classTitle = classEvent.snapshot.child("classtitle").value.toString();

    DatabaseEvent userEvent = await _userRef.child(userId).once();
    String userName = userEvent.snapshot.child('name').value.toString();

    return {'classTitle': classTitle, 'userName': userName};
  }


  Future<Map<String, String?>> getUserData() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? userId = prefs.getString('userId');
    String? email = prefs.getString('userEmail');
    return {'userId': userId, 'userEmail': email};
  }

  Future<String> bookClassInstance(String instanceId) async {

    Map<String, String?> userData = await getUserData();
    String? userId = userData['userId'];
    String? email = userData['userEmail'];

    if (userId == null || email == null) {
      return "User information could not be seek";
    }
    DatabaseReference databaseRef = FirebaseDatabase.instance.ref('bookings').push();
    String bookingId = databaseRef.key!;

    Map<String, dynamic> bookingData = {
      "bookingId": bookingId,
      "userId": userId,
      "contactEmail": email,
      "instanceId": instanceId,
      "status": "Pending",
    };

    try {
      await databaseRef.set(bookingData);
      return "Booking successful";
    } catch (e) {
      return "Booking failed: $e";
    }
  }

  void deleteBooking(Booking booking) async {
    try {
      DatabaseReference databaseRef = FirebaseDatabase.instance.ref('bookings').child(booking.bookingId);
      await databaseRef.remove();

      print('Booking deleted successfully!');
    } catch (e) {
      print('Error while deleting booking: $e');
    }
  }


}


