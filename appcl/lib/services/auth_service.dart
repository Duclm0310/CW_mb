// import 'package:firebase_auth/firebase_auth.dart';
// import 'package:firebase_database/firebase_database.dart';
// import 'package:shared_preferences/shared_preferences.dart';
//
// class AuthService {
//   final FirebaseAuth _auth = FirebaseAuth.instance;
//   final DatabaseReference _database = FirebaseDatabase.instance.ref("users");
//
//   Future<String> login(String email, String password) async {
//     try {
//       //  Firebase Authentication
//       UserCredential userCredential = await _auth.signInWithEmailAndPassword(
//         email: email,
//         password: password,
//       );
//       String uid = userCredential.user!.uid;
//       DatabaseEvent event = await _database.child(uid).once();
//       if (event.snapshot.exists) {
//         final userData = event.snapshot.value;
//         print("User data: $userData");
//
//         SharedPreferences prefs = await SharedPreferences.getInstance();
//         await prefs.setString('userId', uid);
//         await prefs.setString('userEmail', email);
//
//         return "success";
//       } else {
//         return "User data not found in database.";
//       }
//     } catch (e) {
//       return "Error: $e";
//     }
//   }
//
//   Future<String> registerUser(String email, String password, String name) async {
//     if (email.isEmpty || password.isEmpty || name.isEmpty) {
//       return "Please fill in all fields.";
//     }
//
//     try {
//       UserCredential userCredential = await _auth.createUserWithEmailAndPassword(
//         email: email,
//         password: password,
//       );
//
//       String uid = userCredential.user!.uid;
//       final snapshot = await _database.once();
//       String nextId = ((snapshot.snapshot.children.length) + 1) as String;
//       await _database.child(nextId.toString()).set({
//         "id": nextId,
//         "email": email,
//         "name": name,
//         "role": "User",
//         "password": password,
//       });
//
//
//       SharedPreferences prefs = await SharedPreferences.getInstance();
//       await prefs.setString('userId', nextId);
//       await prefs.setString('userEmail', email);
//       await prefs.setString('userName', name);
//
//       return "success"; // Đăng ký thành công
//     } catch (e) {
//       return "Error: $e"; // Trả về thông báo lỗi
//     }
//   }
//
// }
//


import 'package:firebase_database/firebase_database.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AuthService {
  final DatabaseReference _database = FirebaseDatabase.instance.ref("users");

  Future<String> registerUser(String email, String password, String name) async {
    if (email.isEmpty || password.isEmpty || name.isEmpty) {
      return "Please fill in all fields.";
    }

    try {
      final snapshot = await _database.once();
      String nextId = ((snapshot.snapshot.children.length) + 1).toString();

      DatabaseEvent existingUser = await _database
          .orderByChild("email")
          .equalTo(email)
          .once();

      if (existingUser.snapshot.exists) {
        return "Email already exists.";
      }


      await _database.child(nextId).set({
        "id": nextId,
        "email": email,
        "name": name,
        "password": password,
        "role": "User",
        "createdAt": DateTime.now().toIso8601String(),
      });


      SharedPreferences prefs = await SharedPreferences.getInstance();
      await prefs.setString('userId', nextId);
      await prefs.setString('userEmail', email);
      await prefs.setString('userName', name);

      return "success";
    } catch (e) {
      return "Error: $e";
    }
  }


  Future<String> login(String email, String password) async {
    try {

      DatabaseEvent event = await _database
          .orderByChild("email")
          .equalTo(email)
          .once();

      if (event.snapshot.exists) {
        Map<dynamic, dynamic> userData =
        event.snapshot.value as Map<dynamic, dynamic>;
        String userKey = userData.keys.first;
        Map<dynamic, dynamic> userDetails = userData[userKey];


        if (userDetails["password"] == password) {

          SharedPreferences prefs = await SharedPreferences.getInstance();
          await prefs.setString('userId', userDetails["id"]);
          await prefs.setString('userEmail', userDetails["email"]);
          await prefs.setString('userName', userDetails["name"]);

          return "success";
        } else {
          return "Incorrect password.";
        }
      } else {
        return "User not found.";
      }
    } catch (e) {
      return "Error: $e";
    }
  }
}
