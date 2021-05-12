<template>
  <div class="sign">
    <img src="@/assets/img/textlogo.png" alt="logo" />
    <p>서비스 이용을 위해 해당 양식을 입력해주세요.</p>
    <div class="form-modal">
      <div class="form-toggle">
        <button id="login-toggle" @click="toggleLogin">LOGIN</button>
        <button id="signup-toggle" @click="toggleSignup">SIGNUP</button>
      </div>

      <div id="login-form">
        <form>
          <div class="input-each">
            <input ref="loginId" type="text" placeholder="E-MAIL" v-model="Lemail" @keyup.enter="$refs.loginPassword.focus()" required />
          </div>
          <div class="input-each">
            <input ref="loginPassword" type="password" placeholder="PASSWORD" v-model="Lpassword" required @keyup.enter="signinUser" />
          </div>
          <button type="button" class="btn login" @click="signinUser">LOGIN</button>
        </form>
      </div>

      <div id="signup-form">
        <form>
          <input ref="signupInput2" type="text" placeholder="E-MAIL" v-model="email" @keyup.enter="$refs.signupInput3.focus()" required />
          <input ref="signupInput1" type="text" placeholder="NAME" v-model="name" @keyup.enter="$refs.signupInput2.focus()" required />
          <input ref="signupInput3" type="password" placeholder="PASSWORD" v-model="password" @keyup.enter="$refs.signupInput4.focus()" required />
          <input ref="signupInput4" type="password" placeholder="CONFIRM PASSWORD" v-model="pwdcheck" @keyup.enter="signup" required />
          <button type="button" class="btn signup" @click="signup">SIGN UP</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
class AlertRabbitMQSocket {
  constructor(uid) {
    this.uid = uid;
    let ws = new WebSocket('ws://k4d106.p.ssafy.io:15674/ws');
    this.client = Stomp.over(ws);
    this.client.debug = () => {};
    this.login = 'illeum-guest';
    this.passcode = 'illeum-guest';
  }

  connect() {
    this.client.connect(
      this.login,
      this.passcode,
      () => {
        this.client.subscribe(`/amq/queue/member.${this.uid}`, (res) => {
          push.create(res.body);
        }); //큐명을 지정한경우 시 사용
      },
      (error) => {
        console.log('소켓 연결 실패', error);
        console.dir(error);
      },
      '/'
    );
  }
  disconnect() {
    this.client.disconnect();
    // this.client.close();
  }
}
import { registerUser } from '@/api/auth';
import push from 'push.js';

export default {
  name: 'Sign',
  data() {
    return {
      url: '',
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
    // signupClick() {
    //   const loginText = document.querySelector('.title-text .login');
    //   const loginForm = document.querySelector('form.login');
    //   loginForm.style.marginLeft = '-50%';
    //   loginText.style.marginLeft = '-50%';
    // },

    // loginClick() {
    //   const loginText = document.querySelector('.title-text .login');
    //   const loginForm = document.querySelector('form.login');
    //   loginForm.style.marginLeft = '0%';
    //   loginText.style.marginLeft = '0%';
    // },

    // signuplink() {
    //   const signupBtn = document.querySelector('label.signup');
    //   signupBtn.click();
    //   return false;
    // },
    toggleSignup() {
      document.getElementById('login-toggle').style.backgroundColor = '#fff';
      document.getElementById('login-toggle').style.color = '#222';
      document.getElementById('signup-toggle').style.backgroundColor = '#38A897';
      document.getElementById('signup-toggle').style.color = '#fff';
      document.getElementById('login-form').style.display = 'none';
      document.getElementById('signup-form').style.display = 'block';
    },

    toggleLogin() {
      document.getElementById('login-toggle').style.backgroundColor = '#38A897';
      document.getElementById('login-toggle').style.color = '#fff';
      document.getElementById('signup-toggle').style.backgroundColor = '#fff';
      document.getElementById('signup-toggle').style.color = '#222';
      document.getElementById('signup-form').style.display = 'none';
      document.getElementById('login-form').style.display = 'block';
    },

    //validation check
    validateEmail(value) {
      if (/^(?=.{0,30}[A-Za-z])(?=.{0,30}\d)(?=.{0,30}[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,100}$/.test(value)) {
        this.msg['email'] = true;
      } else {
        this.msg['email'] = false;
      }
    },
    validatePassword(value) {
      if (/^(?=.{0,30}\d)(?=.{0,30}[a-z])(?=.{0,30}[^\w\d\s:])([^\s]){8,16}$/.test(value)) {
        this.msg['password'] = true;
      } else {
        this.msg['password'] = false;
      }
    },
    //통신
    async signup() {
      const userData = {
        email: this.email,
        name: this.name,
        thumbnail: 'string',
        password: this.password,
      };
      try {
        const { data } = await registerUser(userData);
        console.log(data);
        if (data.success === true) {
          this.$swal({
            icon: 'success',
            title: '회원가입성공!!',
            showConfirmButton: false,
            timer: 1500,
          });
          // this.$router.push('/sign');
          this.$router.go();
        } else {
          this.$swal({
            icon: 'error',
            title: '회원가입 실패 관리자에게 문의해주세요',
          });
        }
      } catch (error) {
        this.$swal({
          icon: 'error',
          title: '회원가입 실패 관리자에게 문의해주세요',
        });
      }
    },
    async signinUser() {
      if (this.Lemail == '') {
        this.$swal({
          icon: 'error',
          title: '아이디를 입력해 주세요!',
        });
      } else if (this.Lpassword == '') {
        this.$swal({
          icon: 'error',
          title: '비밀번호를 입력해 주세요!',
        });
      } else {
        const userData = {
          email: this.Lemail,
          password: this.Lpassword,
        };
        // console.log(userData);
        await this.$store.dispatch('LOGIN', userData);
        console.log(this.$store.state.token);
        console.log(this.$store.state.name);
        console.log(this.$store.state.role);
        const mqSocket = new AlertRabbitMQSocket(this.$store.state.uuid);
        this.$store.commit('setAlertSocket', mqSocket);
        mqSocket.connect();
      }
    },
  },
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css?family=Montserrat|Quicksand');
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'quicksand', Arial, Helvetica, sans-serif;
}

.sign {
  /* display: grid; */
  height: 100%;
  width: 100%;
  place-items: center;
  background: no-repeat center/100% url('../../assets/img/bg4.jpg');
  /* background: -webkit-linear-gradient(90deg, #0162c8, #55e7fc); */
}
::selection {
  background: #6173ff;
  color: #fff;
}

img {
  margin-top: 5%;
  width: 15vw;
}
.sign p {
  font-weight: 900;
  margin: 10px 0px;
}
.form-modal {
  position: relative;
  width: 450px;
  height: auto;
  margin-top: 2%;
  left: 50%;
  transform: translateX(-50%);
  background: #fff;
  border-top-right-radius: 20px;
  border-top-left-radius: 20px;
  border-bottom-right-radius: 20px;
  box-shadow: 0 3px 20px 0px rgba(0, 0, 0, 0.1);
}

.form-modal button {
  cursor: pointer;
  position: relative;
  text-transform: capitalize;
  font-size: 1em;
  z-index: 2;
  outline: none;
  background: #fff;
  transition: 0.2s;
}

.form-modal .btn {
  border-radius: 20px;
  border: none;
  font-weight: bold;
  font-size: 1.2em;
  padding: 0.8em 1em 0.8em 1em !important;
  transition: 0.5s;
  border: 1px solid #ebebeb;
  margin-bottom: 0.5em;
  margin-top: 0.5em;
  letter-spacing: 5px;
}

.form-modal .login,
.form-modal .signup {
  background: #38a897;
  color: #fff;
}

.form-modal .login:hover,
.form-modal .signup:hover {
  background: #6173ff;
}

.form-toggle {
  position: relative;
  width: 100%;
  height: auto;
}

.form-toggle button {
  width: 50%;
  float: left;
  padding: 1.5em;
  margin-bottom: 1.5em;
  border: none;
  transition: 0.2s;
  font-size: 1.1em;
  font-weight: bold;
  border-top-right-radius: 20px;
  border-top-left-radius: 20px;
}

.form-toggle button:nth-child(1) {
  border-bottom-right-radius: 20px;
}

.form-toggle button:nth-child(2) {
  border-bottom-left-radius: 20px;
}

#login-toggle {
  background: #38a897;
  color: #ffff;
  letter-spacing: 3px;
}
#signup-toggle {
  /* background: #38a897; */
  color: #000;
  letter-spacing: 3px;
}

.form-modal form {
  position: relative;
  width: 90%;
  height: auto;
  left: 50%;
  transform: translateX(-50%);
}

#login-form,
#signup-form {
  position: relative;
  width: 100%;
  height: auto;
  padding-bottom: 1em;
}

#signup-form {
  display: none;
}

#login-form button,
#signup-form button {
  width: 100%;
  margin-top: 0.5em;
  padding: 0.6em;
}

.form-modal input {
  position: relative;
  width: 100%;
  font-size: 1em;
  padding: 1.2em 1.7em 1.2em 1.7em;
  margin-top: 0.6em;
  margin-bottom: 0.6em;
  border-radius: 20px;
  border: none;
  background: #ebebeb;
  outline: none;
  font-weight: bold;
  transition: 0.4s;
}

.form-modal input:focus,
.form-modal input:active {
  transform: scaleX(1.02);
}

.form-modal input::-webkit-input-placeholder {
  color: #777777;
}

@media only screen and (max-width: 500px) {
  .form-modal {
    width: 100%;
  }
}

@media only screen and (max-width: 400px) {
  i {
    display: none !important;
  }
}
</style>
