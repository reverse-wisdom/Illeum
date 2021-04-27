<template>
  <div class="sign">
    <div class="wrapper">
      <div class="title-text">
        <div class="title login">LOGIN</div>
        <div class="title signup">SIGN UP</div>
      </div>
      <div class="form-container">
        <div class="slide-controls">
          <input type="radio" name="slide" id="login" checked />
          <input type="radio" name="slide" id="signup" />
          <label @click="loginClick" for="login" class="slide login">LOGIN</label>
          <label @click="signupClick" for="signup" class="slide signup">REGISTER</label>
          <div class="slider-tab"></div>
        </div>
        <div class="form-inner">
          <form action="#" class="login">
            <div class="field">
              <input type="text" placeholder="Email" v-model="Lemail" required />
            </div>
            <div class="field">
              <input type="password" placeholder="Password" v-model="Lpassword" required />
            </div>
            <div class="pass-link">
              <!-- <a href="#">Forgot password?</a> -->
            </div>
            <div class="field btn">
              <div class="btn-layer"></div>
              <input type="submit" value="LOGIN" @click="login" />
            </div>
            <!-- <div class="signup-link">
              <a @click="signuplink" href="">Signup now</a>
            </div> -->
          </form>
          <form action="#" class="signup">
            <div class="field">
              <input type="text" placeholder="Name" v-model="name" required />
            </div>
            <div class="field">
              <input type="text" placeholder="Email Address" v-model="email" required />
            </div>
            <div class="field">
              <input type="password" placeholder="Password" v-model="password" required />
            </div>
            <div class="field">
              <input type="password" placeholder="Confirm password" v-model="pwdcheck" required />
            </div>
            <div class="field btn">
              <div class="btn-layer"></div>
              <input type="submit" value="SIGN UP" @click="signup" />
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { registerUser } from '@/api/auth';
export default {
  name: 'Sign',
  data() {
    return {
      Lemail: '',
      Lpassword: '',
      name: '',
      email: '',
      password: '',
      pwdcheck: '',
      msg: [],
    };
  },
  methods: {
    //toggle
    signupClick() {
      const loginText = document.querySelector('.title-text .login');
      const loginForm = document.querySelector('form.login');
      loginForm.style.marginLeft = '-50%';
      loginText.style.marginLeft = '-50%';
    },

    loginClick() {
      const loginText = document.querySelector('.title-text .login');
      const loginForm = document.querySelector('form.login');
      loginForm.style.marginLeft = '0%';
      loginText.style.marginLeft = '0%';
    },

    signuplink() {
      const signupBtn = document.querySelector('label.signup');
      signupBtn.click();
      return false;
    },
    //validation check
    validateEmail(value) {
      if (
        /^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test(
          value
        )
      ) {
        this.msg['email'] = true;
      } else {
        this.msg['email'] = false;
      }
    },
    validatePassword(value) {
      if (/^(?=.*\d)(?=.*[a-z])(?=.*[^\w\d\s:])([^\s]){8,16}$/.test(value)) {
        this.msg['password'] = true;
      } else {
        this.msg['password'] = false;
      }
    },
    //통신
    async signup() {
      // if (!this.msg['email']) {
      //   this.$swal({
      //     icon: 'error',
      //     title: '이메일 형식이 잘못되었습니다.!',
      //   });
      // } else if (this.nickname == null) {
      //   this.$swal({
      //     icon: 'error',
      //     title: '닉네임을 입력해주세요!',
      //   });
      // } else if (!this.msg['password']) {
      //   this.$swal({
      //     icon: 'error',
      //     title: '비밀번호 입력시 8자리이상, 특수문자를 포함해주세요!',
      //   });
      // } else {
      //   const userData = {
      //     name: this.name,
      //     email: this.email,
      //     password: this.password,
      //   };
      //   const { data } = await register(userData);

      //   if (data == 'SUCCESS') {
      //     this.$swal({
      //       position: 'top-end',
      //       icon: 'success',
      //       title: '회원가입성공!!',
      //       showConfirmButton: false,
      //       timer: 1500,
      //     });
      //     this.$router.push('/');
      //   } else {
      //     this.$swal({
      //       icon: 'error',
      //       title: '회원가입 실패 관리자에게 문의해주세요',
      //     });
      //   }
      // }

      const userData = {
        email: this.email,
        name: this.name,
        thumbnail: 'string',
        password: this.password,
      };
      const { data } = await register(userData);
      console.log(data);
      this.$router.push('/sign');
    },
    async login() {
      if (this.Lemail == null) {
        this.$swal({
          icon: 'error',
          title: '아이디를 입력해 주세요!',
        });
      } else if (this.Lpassword == null) {
        this.$swal({
          icon: 'error',
          title: '비밀번호를 입력해 주세요!',
        });
      } else {
        const userData = {
          email: this.email,
          password: this.password,
        };
        this.$store.dispatch('LOGIN', userData);
      }
    },
  },
};
</script>

<style>
@import url('https://fonts.googleapis.com/css?family=Poppins:400,500,600,700&display=swap');
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Poppins', sans-serif;
}
.sign {
  display: grid;
  height: 100%;
  width: 100%;
  place-items: center;
  background: no-repeat center/100% url('../../assets/img/signbg.jpg');
  /* background: -webkit-linear-gradient(90deg, #0162c8, #55e7fc); */
}
::selection {
  background: #fa4299;
  color: #fff;
}
.wrapper {
  overflow: hidden;
  max-width: 390px;
  background: #fff;
  padding: 30px;
  border-radius: 40px;
  box-shadow: 13px 13px 20px #cbced1, -13px -13px 20px #ffffff;
}
.wrapper .title-text {
  display: flex;
  width: 200%;
}
.wrapper .title {
  width: 50%;
  font-size: 35px;
  font-weight: 600;
  text-align: center;
  transition: all 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
.wrapper .slide-controls {
  position: relative;
  display: flex;
  height: 50px;
  width: 100%;
  overflow: hidden;
  margin: 30px 0 10px 0;
  justify-content: space-between;
  border: 1px solid lightgrey;
  border-radius: 5px;
}
.slide-controls .slide {
  height: 100%;
  width: 100%;
  color: #000;
  font-size: 15px;
  font-weight: 500;
  text-align: center;
  line-height: 48px;
  cursor: pointer;
  z-index: 1;
  transition: all 0.6s ease;
}
.slide-controls label.signup {
  color: #000;
}
.slide-controls .slider-tab {
  position: absolute;
  height: 100%;
  width: 50%;
  left: 0;
  z-index: 0;
  border-radius: 5px;
  background: #1cbd8f;
  transition: all 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
input[type='radio'] {
  display: none;
}
#signup:checked ~ .slider-tab {
  left: 50%;
}
#signup:checked ~ label.signup {
  color: #000;
  cursor: default;
  user-select: none;
}
#signup:checked ~ label.login {
  color: #000;
}
#login:checked ~ label.signup {
  color: #000;
}
#login:checked ~ label.login {
  cursor: default;
  user-select: none;
}
.wrapper .form-container {
  width: 100%;
  overflow: hidden;
}
.form-container .form-inner {
  display: flex;
  width: 200%;
}
.form-container .form-inner form {
  width: 50%;
  transition: all 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
.form-inner form .field {
  height: 50px;
  width: 100%;
  margin-top: 20px;
}
.form-inner form .field input {
  height: 100%;
  width: 100%;
  outline: none;
  padding-left: 15px;
  border-radius: 5px;
  border: 1px solid lightgrey;
  border-bottom-width: 2px;
  font-size: 12px;
  transition: all 0.3s ease;
}
.form-inner form .field input:focus {
  border-color: #3a7bd5;
  /* box-shadow: inset 0 0 3px #fb6aae; */
}
.form-inner form .field input::placeholder {
  color: #999;
  transition: all 0.3s ease;
}
form .field input:focus::placeholder {
  color: #b3b3b3;
}
.form-inner form .pass-link {
  margin-top: 5px;
}
.form-inner form .signup-link {
  text-align: center;
  margin-top: 30px;
}
.form-inner form .pass-link a,
.form-inner form .signup-link a {
  color: #3a7bd5;
  text-decoration: none;
}
.form-inner form .pass-link a:hover,
.form-inner form .signup-link a:hover {
  text-decoration: underline;
}
form .btn {
  height: 50px;
  width: 100%;
  border-radius: 5px;
  position: relative;
  overflow: hidden;
}
form .btn .btn-layer {
  height: 100%;
  width: 300%;
  position: absolute;
  left: -100%;
  background: #1cbd8f;
  border-radius: 5px;
  transition: all 0.4s ease;
}
form .btn:hover .btn-layer {
  left: 0;
}
form .btn input[type='submit'] {
  height: 100%;
  width: 100%;
  z-index: 1;
  position: relative;
  background: none;
  border: none;
  color: #fff;
  padding-left: 0;
  border-radius: 5px;
  font-size: 20px;
  font-weight: 500;
  cursor: pointer;
}
</style>
