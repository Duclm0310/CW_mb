import 'package:firebase_database/firebase_database.dart';
import '../models/ClassInstance.dart';

class ClassInstanceService {
  final DatabaseReference _classInstanceRef = FirebaseDatabase.instance.ref("classInstances");
  final DatabaseReference _classRef = FirebaseDatabase.instance.ref("yoga_classes");
  final DatabaseReference _userRef = FirebaseDatabase.instance.ref("users");
  
  Future<List<ClassInstance>> getClassInstances() async {
    try {
      DatabaseEvent classInstanceEvent = await _classInstanceRef.once();

      if (classInstanceEvent.snapshot.exists) {
        List<ClassInstance> instances = [];

        for (var child in classInstanceEvent.snapshot.children) {
          String classId = child.child('classId').value.toString();
          String userId = child.child('userId').value.toString();

          DatabaseEvent classEvent = await _classRef.child(classId).once();
          String classTitle = classEvent.snapshot.child('classtitle').value.toString();

          DatabaseEvent userEvent = await _userRef.child(userId).once();
          String userName = userEvent.snapshot.child('name').value.toString();

          instances.add(ClassInstance.fromSnapshot(child, classTitle, userName));
        }

        return instances;
      } else {
        return [];
      }
    } catch (e) {
      throw Exception("Error fetching class instances: $e");
    }
  }

  Future<List<ClassInstance>> getClassInstancesByClassId(String classId) async {
    DatabaseEvent event = await _classInstanceRef.once();
    List<ClassInstance> instances = [];

    if (event.snapshot.exists) {
      for (var child in event.snapshot.children) {
        String instanceClassId = child.child('classId').value.toString();

        if (instanceClassId == classId) {
          String userId = child.child('userId').value.toString();

          DatabaseEvent classEvent = await _classRef.child(instanceClassId).once();
          String classTitle = classEvent.snapshot.child('classtitle').value.toString();

          DatabaseEvent userEvent = await _userRef.child(userId).once();
          String userName = userEvent.snapshot.child('name').value.toString();

          var instance = ClassInstance.fromSnapshot(child, classTitle, userName);
          instances.add(instance);
        }
      }
    }

    return instances;
  }



  Future<ClassInstance?> getClassInstancesById(String classId) async {
    DatabaseEvent event = await _classInstanceRef.once();
    ClassInstance instance;

    if (event.snapshot.exists) {
      for (var child in event.snapshot.children) {
        String instanceClassId = child.child('instanceId').value.toString();

        if (instanceClassId == classId) {
          String userId = child.child('userId').value.toString();
          String classId = child.child('classId').value.toString();

          DatabaseEvent classEvent = await _classRef.child(classId).once();
          String classTitle = classEvent.snapshot.child('classtitle').value.toString();

          DatabaseEvent userEvent = await _userRef.child(userId).once();
          String userName = userEvent.snapshot.child('name').value.toString();

          return instance = ClassInstance.fromSnapshot(child, classTitle, userName);
        }
      }
    }
    return null;
  }

}
