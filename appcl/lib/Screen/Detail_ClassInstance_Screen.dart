import 'package:appcl/models/YogaClass.dart';
import 'package:appcl/models/user.dart';
import 'package:appcl/services/user_service.dart';
import 'package:appcl/services/yogaclass_service.dart';
import 'package:appcl/widgets/TeacherDetail.dart';
import 'package:flutter/material.dart';
import '../helper/SharedPreferencesHelper.dart';
import '../models/ClassInstance.dart';
import '../services/booking_service.dart';
import '../widgets/YogaClassDetail.dart';



class DetailClassinstanceScreen extends StatefulWidget {
  final ClassInstance instance;

  const DetailClassinstanceScreen({Key? key, required this.instance}) : super(key: key);

  @override
  State<DetailClassinstanceScreen> createState() => _DetailClassinstanceScreenState();
}

class _DetailClassinstanceScreenState extends State<DetailClassinstanceScreen> {
  Future<User?>? user;
  Future<YogaClass?>? yogaclass;
  double? price;
  @override
  void initState() {
    super.initState();
    yogaclass = YogaclassService().getYogaById(widget.instance.classId);
    user = UserService().getUserById(widget.instance.userId);
    _loadPrice();

  }
  Future<void> _loadPrice() async {
    price = await YogaclassService().getPricebyId(widget.instance.classId);
    setState(() {});
  }





  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.instance.classTitle),
      ),
      body: Center(
        child: Padding(
          padding: EdgeInsets.all(10),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Container(
                height: 160,
                width: 300,
                margin: EdgeInsets.only(bottom: 10),
                decoration: BoxDecoration(
                  color: Colors.grey.withOpacity(0.1),
                  borderRadius: BorderRadius.circular(16),
                ),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      "Price: ${price} ",
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                        color: Colors.purple.withOpacity(1),
                      ),
                    ),
                    SizedBox(height: 10),
                    Text(
                      widget.instance.classTitle,
                      style: TextStyle(
                        fontSize: 43,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ],
                ),
              ),
              SizedBox(height: 10),
              Expanded(
                child: DefaultTabController(
                  length: 2,
                  child: Column(
                    children: [
                      TabBar(
                        labelColor: Colors.purple,
                        indicatorColor: Colors.purple,
                        tabs: [
                          Tab(text: "Details"),
                          Tab(text: "Teacher"),
                        ],
                      ),
                      Expanded(
                        child: TabBarView(
                          children: [
                            FutureBuilder<YogaClass?>(
                              future: yogaclass,
                              builder: (context, snapshot) {
                                if (snapshot.connectionState ==
                                    ConnectionState.waiting) {
                                  return Center(
                                      child: CircularProgressIndicator());
                                } else if (snapshot.hasError) {
                                  return Center(child: Text(
                                      "An error occurred while loading data."));
                                } else if (!snapshot.hasData ||
                                    snapshot.data == null) {
                                  return Center(child: Text(
                                      "No class data found"));
                                } else {
                                  final yogaClassData = snapshot.data!;
                                  return YogaClassDetail(
                                      yogaClassData: yogaClassData); // Chuyển yogaClassData vào widget YogaClassDetail
                                }
                              },
                            ),
                            FutureBuilder<User?>(
                              future: user,
                              builder: (context, snapshot) {
                                if (snapshot.connectionState ==
                                    ConnectionState.waiting) {
                                  return Center(
                                      child: CircularProgressIndicator());
                                } else if (snapshot.hasError) {
                                  return Center(child: Text(
                                      "An error occurred while loading data."));
                                } else if (!snapshot.hasData ||
                                    snapshot.data == null) {
                                  return Center(child: Text(
                                      "No user data found"));
                                } else {
                                  final userdata = snapshot.data!;
                                  return Teacherdetail(
                                      teacher:  userdata );
                                }
                              },
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
              ),
              SizedBox(height: 10),
              Container(
                height: 150,
                width: 400,
                decoration: BoxDecoration(
                  color: Colors.purpleAccent.withOpacity(0.05),
                  borderRadius: BorderRadius.only(
                    bottomRight: Radius.circular(16),
                    bottomLeft: Radius.circular(16),
                  ),
                ),
                child: Padding(
                  padding: EdgeInsets.all(10),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      GestureDetector(
                      onTap: () async {
                               String message = await BookingService().bookClassInstance(widget.instance.instanceId);
                                 ScaffoldMessenger.of(context).showSnackBar(
                                 SnackBar(content: Text(message)),
                               );
                              },
                        child: Container(
                          padding: EdgeInsets.all(10),
                          decoration: BoxDecoration(
                            color: Colors.purpleAccent,
                            borderRadius: BorderRadius.circular(16),
                          ),
                          child: Text(
                            "Book this Instance",
                            style: TextStyle(
                                fontSize: 20, fontWeight: FontWeight.bold),
                          ),
                        ),
                      ),
                    ],
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

