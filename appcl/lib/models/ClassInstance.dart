import 'package:firebase_database/firebase_database.dart';
import 'package:intl/intl.dart';

class ClassInstance {
  final String instanceId;
  final String classId;
  final String userId;
  final String comments;
  final int day;
  final int month;
  final num year;
  final String classTitle;
  final String userName;

  ClassInstance({
    required this.instanceId,
    required this.classId,
    required this.userId,
    required this.comments,
    required this.day,
    required this.month,
    required this.year,
    required this.classTitle,
    required this.userName,
  });


  factory ClassInstance.fromSnapshot(DataSnapshot snapshot, String classTitle, String userName) {
    var sessionDate = snapshot.child('sessionDate').value as Map<dynamic, dynamic>;

    int day = sessionDate['date'];
    int month = sessionDate['month'] + 1;
    num year = 1900 + sessionDate['year'];

    return ClassInstance(
      instanceId: snapshot.child('instanceId').value.toString(),
      classId: snapshot.child('classId').value.toString(),
      userId: snapshot.child('userId').value.toString(),
      comments: snapshot.child('comments').value.toString(),
      day: day,
      month: month,
      year: year,
      classTitle: classTitle,
      userName: userName,
    );
  }

  String get formattedDate {
    return '$day/${month.toString().padLeft(2, '0')}/$year';
  }
}
