

import 'package:appcl/models/YogaClass.dart';
import 'package:appcl/models/user.dart';
import 'package:firebase_database/firebase_database.dart';

class YogaclassService{
  final DatabaseReference _yogaRef = FirebaseDatabase.instance.ref("yoga_classes");

  Future<List<YogaClass>> getYogaClass() async {
    DatabaseEvent event = await _yogaRef.once();
    final data = event.snapshot.value;
    List<YogaClass> yogaclasses = [];

    if(data is Map<String, dynamic>){
      data.forEach((key, value){
        yogaclasses.add(YogaClass.fromMap(Map<String, dynamic>.from(value)));
      });
    } else if (data is List){
      for (var item in data) {
        if (item != null) {
          yogaclasses.add(YogaClass.fromMap(Map<String, dynamic>.from(item)));
        }
      }
    }
return yogaclasses;
  }

  Future<YogaClass?> getYogaById(String yogaId) async {
    DatabaseEvent event = await _yogaRef.child(yogaId).once();
    if (event.snapshot.value != null) {
      return YogaClass.fromMap(Map<String, dynamic>.from(event.snapshot.value as Map));
    }
  }

  Future<List<YogaClass>> getYogaClassesByDayOrTime(String? dayOfWeek, String? time) async {
    DatabaseEvent event = await _yogaRef.once();
    final data = event.snapshot.value;
    List<YogaClass> filteredClasses = [];

    if (data is Map<String, dynamic>) {
      data.forEach((key, value) {
        var yogaClass = YogaClass.fromMap(Map<String, dynamic>.from(value));
        if ((dayOfWeek != null && yogaClass.day == dayOfWeek) ||
            (time != null && yogaClass.time == time)) {
          filteredClasses.add(yogaClass);
        }
      });
    }

    return filteredClasses;
  }

  Future<List<YogaClass>> getYogaClassesByDay(String? dayOfWeek) async {
    List<YogaClass> filteredClasses = [];

    try {
      DatabaseEvent event = await _yogaRef.once();
      final data = event.snapshot.value;
      if (data is Map) {
        data.forEach((key, value) {
          var yogaClass = YogaClass.fromMap(Map<String, dynamic>.from(value));
          if (dayOfWeek != null && yogaClass.day == dayOfWeek) {
            filteredClasses.add(yogaClass);
          }
        });
      } else {
        print("No classes found in the data or data format is incorrect");
      }
    } catch (e) {
      print("Error fetching classes: $e");
    }
    return filteredClasses;
  }

  Future<double?> getPricebyId(String classId) async {
    DatabaseEvent event = await _yogaRef.once();
    final data = event.snapshot.value;
    double? price;

    if (data is Map) {
      for (var value in data.values) {
        var yogaClass = YogaClass.fromMap(Map<String, dynamic>.from(value));
        if (classId.isNotEmpty && yogaClass.classId == classId) {
          price = yogaClass.price;
          break;
        }
      }
    } else {
      print("No classes found in the data or data format is incorrect");
    }
    return price;
  }

}

