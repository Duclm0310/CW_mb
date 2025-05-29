import 'package:firebase_database/firebase_database.dart';

class Booking {
  final String bookingId;
  final String contactEmail;
  final String instanceId;
  final String status;
  final String userId;
  final String userName;
  final String classTitle;

  Booking({
    required this.bookingId,
    required this.contactEmail,
    required this.instanceId,
    required this.status,
    required this.userId,
    required this.userName,
    required this.classTitle,
  });

  factory Booking.fromSnapshot(DataSnapshot snapshot, String classTitle, String userName) {
    return Booking(
      bookingId: snapshot.child('bookingId').value.toString(),
      contactEmail: snapshot.child('contactEmail').value.toString(),
      instanceId: snapshot.child('instanceId').value.toString(),
      status: snapshot.child('status').value.toString(),
      userId: snapshot.child('userId').value.toString(),
      userName: userName,
      classTitle: classTitle,
    );
  }
}
