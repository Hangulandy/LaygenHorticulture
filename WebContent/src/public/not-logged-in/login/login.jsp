<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<h1>${dict.get('login', lang)}</h1>
<table class="transparent">
	<tr class="transparent">
		<td class="transparent">${dict.get('emailLabel', lang) } :</td>
		<td class="transparent"><input type="text"
			placeholder="${dict.get('emailPlaceholder', lang) }" ng-model="loginCtrl.email"></td>
	</tr>
	<tr class="transparent">
		<td class="transparent">${dict.get('passwordLabel', lang) } :</td>
		<td class="transparent"><input type="password"
			ng-model="loginCtrl.password"></td>
	</tr>
	<tr class="transparent">
		<td class="transparent"></td>
		<td class="transparent"><button ng-click="loginCtrl.login();">${dict.get('login', lang)}</button></td>
	</tr>
</table>

<div ng-if="loginCtrl.user.name !== undefined">{{loginCtrl.user.name}}</div>
<div ng-if="loginCtrl.user.username !== undefined">{{loginCtrl.user.username}}</div>
