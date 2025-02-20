import { defineComponent, h } from "vue";
import { RouteRecordRaw, RouterView } from "vue-router";
import AdminDashboard from "@/views/admin/AdminDashboard.vue";
import ManageUsers from "@/views/admin/ManageUsers.vue";
import ManageBoards from "@/views/admin/ManageBoards.vue";

export const adminRoutes: Array<RouteRecordRaw> = [
  {
    path: "/admin",
    name: "Admin",
    component: defineComponent({
      name: "AdminParentInline",
      setup() {
        return () => h(RouterView);
      }
    }),
    meta: { requiresAdmin: true },
    children: [
      {
        path: "",
        name: "AdminDashboard",
        component: AdminDashboard
      },
      {
        path: "users",
        name: "ManageUsers",
        component: ManageUsers
      },
      {
        path: "boards",
        name: "ManageBoards",
        component: ManageBoards
      }
    ]
  }
];
