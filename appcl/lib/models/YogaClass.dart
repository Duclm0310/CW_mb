import 'package:firebase_database/firebase_database.dart';


class YogaClass {
  final String classId;
  final String classTitle;
  final String classType;
  final String day;
  final String description;
  final int duration;
  final double price;
  final String time;
  final int capacity;

  YogaClass({
    required this.classId,
    required this.classTitle,
    required this.classType,
    required this.day,
    required this.description,
    required this.duration,
    required this.price,
    required this.time,
    required this.capacity,
  });

  factory YogaClass.fromMap(Map<String, dynamic> map) {
    return YogaClass(
      classId: map['classid']?.toString() ?? '',
      classTitle: map['classtitle']?.toString() ?? '',
      classType: map['classtype']?.toString() ?? '',
      day: map['day']?.toString() ?? '',
      description: map['description']?.toString() ?? '',
      duration: int.parse(map['duration']?.toString() ?? '0'),
      price: double.parse(map['price']?.toString() ?? '0.0'),
      time: map['time']?.toString() ?? '',
      capacity: int.parse(map['capacity']?.toString() ?? '0'),
    );
  }
}
