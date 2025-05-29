import 'package:appcl/Screen/SearchInstanceClass.dart';
import 'package:appcl/services/auth_service.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../models/ClassInstance.dart';
import '../services/class_instance_service.dart';
import '../services/user_service.dart';
import '../widgets/ClassInstanceItem.dart';
import 'Detail_ClassInstance_Screen.dart';

class Mainapp extends StatefulWidget {
  const Mainapp({super.key});

  @override
  State<Mainapp> createState() => _MainappState();
}

class _MainappState extends State<Mainapp> {
  List<ClassInstance> classInstances = [];
  String errorMessage = "";
  final ClassInstanceService _classInstanceService = ClassInstanceService();
  String? userName;

  @override
  void initState() {
    super.initState();
    _loadClassInstances();
    _loadnameUsercurrent();
  }

  Future<void> _loadClassInstances() async {
    try {
      List<ClassInstance> instances = await _classInstanceService.getClassInstances();
      setState(() {
        classInstances = instances;
      });
    } catch (e) {
      setState(() {
        errorMessage = "Error: $e";
      });
    }
  }

  Future<void> _loadnameUsercurrent() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    String? userId = prefs.getString('userId');

    if (userId != null) {
      var user = await UserService().getUserById(userId);
      setState(() {
        userName = user?.name;
      });
    }


  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Yoga Class",
        style: TextStyle(
          fontSize: 25,
          fontWeight: FontWeight.bold,
          fontStyle: FontStyle.normal
        ),),
      actions: [
        IconButton(
          icon: Icon(Icons.search),
          onPressed: () {
            Navigator.push(
                context,
                MaterialPageRoute(
                builder: (context) => SearchClassInstanceScreen(),
            )
            );
          },
        ),
      ],),
      body: Padding(
        padding: EdgeInsets.all(15),
        child: Padding(
          padding: EdgeInsets.all(10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Container(
                padding: EdgeInsets.all(5),
                // height: 50,
                width: 350,
                decoration: BoxDecoration(
                  color: Colors.blueAccent.withOpacity(0.1),
                  borderRadius: BorderRadius.circular(16)
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Hi, ${userName}",
                      textAlign: TextAlign.left,
                      style: TextStyle(
                        fontSize: 16
                      ),
                    ),
                    SizedBox(height: 10),
                    Text(
                      "How are you now?",
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 24,
                      ),
                    ),
                    SizedBox(height: 10),
                  ],
                ),
              ),
              SizedBox(height: 15),
                Expanded(
                child: errorMessage.isNotEmpty
                    ? Center(child: Text(errorMessage, style: TextStyle(color: Colors.red)))
                    : classInstances.isEmpty
                    ? Center(child: CircularProgressIndicator())
                    : Container(
                  decoration: BoxDecoration(
                    borderRadius: BorderRadius.circular(16),
                    color: Colors.grey.withOpacity(0.05),
                  ),
                  child:
                  ListView.builder(
                    itemCount: classInstances.length,
                    padding: EdgeInsets.symmetric(vertical: 10),
                    itemBuilder: (context, index) {
                      final instance = classInstances[index];
                      return Column(
                        children: [
                          ClassInstanceItem(
                            instance: instance,
                            onTap: () {
                              Navigator.push(
                                context,
                                MaterialPageRoute(
                                  builder: (context) => DetailClassinstanceScreen(instance: instance),
                                ),
                              );
                            },
                          ),
                          SizedBox(height: 10), // Thêm khoảng cách giữa các item
                        ],
                      );
                    },
                  ),

                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
