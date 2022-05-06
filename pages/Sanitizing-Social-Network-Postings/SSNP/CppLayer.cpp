
#include <iostream>

#include <ostream>
#include <algorithm>
#include <fstream>
#include <numeric>
#include <iterator>
#include <vector>
#include <set>
#include "SWI-Prolog.h"
#include "SWI-cpp.h"

using std::ifstream;
using std::istringstream;
using std::istream_iterator;
using std::back_insert_iterator;


using std::vector;
using std::string;

#include "CallCpp.h"




bool initialized = false;
void init() {
    if (!initialized) {
      const char* av[2];
      av[0] = "CppLayer";
      av[1] = "-q";

      const char* dir = "SWI_HOME_DIR=/snap/swi-prolog/current/usr/lib/swipl";
      putenv((char*)dir);

      PL_initialise(2, (char**)av);
      PlCall("consult('info.pl')");
      PlCall("consult('sanitize.pl')");
      initialized = true;
    }
}

foreign_t read_atom_list(term_t list_atoms, vector<string>& elems) {
  term_t head = PL_new_term_ref();
  term_t list = PL_copy_term_ref(list_atoms);
  while (PL_get_list(list, head, list)) {
    char* s;

    //if (PL_get_atom_chars(head, &s)) {
      if (PL_get_chars(head, &s, CVT_WRITE)) {
      elems.push_back(s);
    }
    else {
      PL_fail;
    }
  }
  return PL_get_nil(list);
}


void getPrologResult(const char* nameOne, const char* nameTwo, vector<string> &elems) {
  init();
  term_t a, aa, aaa, b, ans;
  functor_t results;

  a = PL_new_term_ref();
  PL_put_atom_chars(a, nameOne);
  aa = PL_new_term_ref();
  PL_put_atom_chars(aa, nameTwo);
  b = PL_new_term_ref();
  ans = PL_new_term_ref();
  //results = PL_new_functor(PL_new_atom("test"), 2);
  results = PL_new_functor(PL_new_atom("test2"), 3);
  PL_cons_functor(ans, results, a, aa, b);
  //PL_cons_functor(ans, results, a, b);

  module_t info_pl = PL_new_module(PL_new_atom("info.pl"));

  if (PL_call(ans, info_pl))
    read_atom_list(b, elems);
}


  //Class:     CallCpp
  //Method:    callCppMethod
  //Signature: (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
 
JNIEXPORT jobjectArray JNICALL Java_CallCpp_callCppMethod
  (JNIEnv *env, jobject obj, jstring strone, jstring strtwo) {
      //jstring a = env->NewStringUTF()
      //std::cout << "Hello World!";
      int counter = 0;
      vector<string> elems;
      jboolean iscopy;
      const char* strMsg1 = env->GetStringUTFChars(strone, &iscopy);
      const char* strMsg2 = env->GetStringUTFChars(strtwo, &iscopy);
      getPrologResult(strMsg1, strMsg2, elems);
      //getPrologResult("ellen", "rich", elems);

      jclass jStringCls = 0;
      jStringCls = env->FindClass("java/lang/String");

      jobjectArray result;  
      result = env->NewObjectArray(elems.size(), jStringCls, NULL);

      for (vector<string>::iterator t=elems.begin(); t!=elems.end(); ++t) 
    {
        jstring newString = env->NewStringUTF((*t).c_str());
        //jsize length = env->GetArrayLength(buf);
        //pEnv->SetObjectArrayElement(result, i * 2, keyString);
        env->SetObjectArrayElement(result, counter, newString);
        counter++;
        //std::cout<<*t<<'\n';
    }

      return (result);
}

/*
int main(int argc, char** argv) {
  //argv[0] is the executable itself

    vector<string> elems;
    getPrologResult("ellen", elems);
    std::copy(elems.begin(), elems.end(), std::ostream_iterator<string>(std::cout, " "));
    std::cout << std::endl;
    //std::cout << "NICEU!\n";
   return 0;
}
*/